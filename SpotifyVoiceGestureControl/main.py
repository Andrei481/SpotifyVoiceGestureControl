import time

import cv2

from lib.hand_tracking.hand_tracking_module import HandDetector
from lib.voice_recognition.speech_recognition_module import VoiceRecognizer, Commands
from lib.voice_recognition.phrases import *
from api.spotify_client import SpotifyClient


def main():
    pass
    # hands = HandDetector()
    # cap = cv2.VideoCapture(0)
    # previous_time = current_time = 0
    # hands = HandDetector()
    #
    # while True:
    #     success, img = cap.read()
    #     img = cv2.flip(img, 1)  # careful about this and cx, cy -> they are inversed
    #     img = hands.find_hands(img)
    #     img.flags.writeable = False
    #     hands.get_landmarks_list(img)
    #     cv2.imshow("Image", img)
    #     if cv2.waitKey(1) & 0xFF == ord('q'):  # Press 'q' to exit
    #         break
    # cap.release()
    # cv2.destroyAllWindows()
    print("Andrei")
    voice = VoiceRecognizer()
    spotify = SpotifyClient()
    while True:
        voice.command = ""
        voice.recognize_speech()
        if voice.command == Commands.STOP:
            break
        elif voice.command == Commands.PLAY:
            song = str(voice.text).split(play_phrase)[1]
            song_uri = spotify.get_track_uri(song)
            spotify.play(song_uri)
        elif voice.command == Commands.PLAY_PLAYLIST:
            playlist = str(voice.text).split(play_playlist_phrase)[1].strip()
            print(f"|{playlist}|")
            playlist_id = spotify.get_playlist_id(playlist)
            # print(playlist_id)
            spotify.play_playlist(playlist_id)
        elif voice.command == Commands.PAUSE:
            spotify.pause()
        elif voice.command == Commands.RESUME:  # FIX THIS
            spotify.resume_playing()
        elif voice.command == Commands.PREVIOUS:
            spotify.previous_song()
        elif voice.command == Commands.NEXT:
            spotify.skip_song()
        elif voice.command == Commands.VOLUME_INCREASE:
            spotify.set_volume(True)
        elif voice.command == Commands.VOLUME_DECREASE:
            spotify.set_volume(False)


if __name__ == "__main__":
    main()
