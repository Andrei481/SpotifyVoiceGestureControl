import math
import time
import threading
import cv2
import numpy as np

from lib.hand_tracking.hand_tracking_module import HandDetector
from lib.voice_recognition.speech_recognition_module import VoiceRecognizer, Commands
from lib.voice_recognition.phrases import *
from api.spotify_client import SpotifyClient


def main():
    cap = cv2.VideoCapture(0)
    # cap.set(3, camera_width)
    # cap.set(4, camera_height)
    hands = HandDetector()
    previous_time = 0
    spotify = SpotifyClient()
    lock = threading.Lock()

    while True:
        success, img = cap.read()
        img = cv2.flip(img, 1)  # careful about this and cx, cy -> they are inversed
        img = hands.find_hands(img)
        img.flags.writeable = False
        landmark_list = hands.get_landmarks_list(img)
        if landmark_list:
            # print(landmark_list[4], landmark_list[8])
            x1, y1 = landmark_list[4][1], landmark_list[4][2]
            x2, y2 = landmark_list[8][1], landmark_list[8][2]
            cx, cy = (x1 + x2) // 2, (y1 + y2) // 2

            cv2.circle(img, (x1, y1), 15, (255, 255, 0), cv2.FILLED)
            cv2.circle(img, (x2, y2), 15, (255, 255, 0), cv2.FILLED)
            cv2.line(img, (x1, y1), (x2, y2), (255, 0, 0), 2)
            cv2.circle(img, (cx, cy), 15, (255, 0, 255), cv2.FILLED)

            length = math.hypot(x2 - x1, y2 - y1)
            # print(length)
            global vol
            vol = np.interp(length, [9, 143], [0, 100])
            print(vol)
            spotify.set_volume(volume=int(vol))
            current_time = time.time()
            fps = 1 / (current_time - previous_time)
            previous_time = current_time
            cv2.putText(img, f'FPS: {int(fps)}', (50, 50), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 255), 3)
        cv2.imshow("Image", img)
        if cv2.waitKey(1) & 0xFF == ord('q'):  # Press 'q' to exit
            break
    cap.release()
    cv2.destroyAllWindows()
    # print("Speech recognition activated: ")
    # voice = VoiceRecognizer()
    # spotify = SpotifyClient()
    # while True:
    #     voice.command = ""
    #     voice.recognize_speech()
    #     if voice.command == Commands.STOP:
    #         break
    #     elif voice.command == Commands.PLAY:
    #         song = str(voice.text).split(play_phrase)[1]
    #         song_uri = spotify.get_track_uri(song)
    #         spotify.play(song_uri)
    #     elif voice.command == Commands.PLAY_PLAYLIST:
    #         playlist = str(voice.text).split(play_playlist_phrase)[1].strip()
    #         print(f"|{playlist}|")
    #         playlist_id = spotify.get_playlist_id(playlist)
    #         # print(playlist_id)
    #         spotify.play_playlist(playlist_id)
    #     elif voice.command == Commands.PAUSE:
    #         spotify.pause()
    #     elif voice.command == Commands.RESUME:  # FIX THIS
    #         spotify.resume_playing()
    #     elif voice.command == Commands.PREVIOUS:
    #         spotify.previous_song()
    #     elif voice.command == Commands.NEXT:
    #         spotify.skip_song()
    #     elif voice.command == Commands.VOLUME_INCREASE:
    #         spotify.set_volume(True)
    #     elif voice.command == Commands.VOLUME_DECREASE:
    #         spotify.set_volume(False)


if __name__ == "__main__":
    main()
