from enum import Enum

import speech_recognition

from lib import voice_recognition
from lib.voice_recognition import phrases


class Commands(Enum):
    STOP = 0
    PLAY = 1
    PAUSE = 2
    PREVIOUS = 3
    NEXT = 4
    UNKNOWN = 5


def get_command(text):
    if phrases.stop_phrase in text:
        return Commands.STOP
    elif phrases.play_phrase in text:
        return Commands.PLAY
    elif phrases.pause_phrase in text:
        return Commands.PAUSE
    elif phrases.next_phrases[0] in text or phrases.next_phrases[1]:
        return Commands.NEXT
    elif phrases.previous_phrase in text:
        return Commands.PREVIOUS
    else:
        return Commands.UNKNOWN


class VoiceRecognizer:
    command = Commands.UNKNOWN
    text = ""

    def __init__(self, energy_threshold=200, dynamic_energy_threshold=True, dynamic_energy_adjustment_damping=0.15,
                 dynamic_energy_ratio=0.15, pause_threshold=0.8, operation_timeout=None, phrase_threshold=0.1,
                 non_speaking_duration=0.5):
        self.energy_threshold = energy_threshold  # minimum audio energy to consider for recording
        self.dynamic_energy_threshold = dynamic_energy_threshold
        self.dynamic_energy_adjustment_damping = dynamic_energy_adjustment_damping
        self.dynamic_energy_ratio = dynamic_energy_ratio
        self.pause_threshold = pause_threshold  # seconds of non-speaking audio before a phrase is considered complete
        self.operation_timeout = operation_timeout  # seconds after an internal operation (e.g., an API request)
        # starts before it times out, or ``None`` for no timeout

        self.phrase_threshold = phrase_threshold  # minimum seconds of speaking audio before we consider the speaking
        # audio a phrase - values below this are ignored (for filtering out clicks and pops)
        self.non_speaking_duration = non_speaking_duration  # seconds of non-speaking audio to keep on both sides of

        # the recording

    def recognize_speech(self):
        recognizer = speech_recognition.Recognizer()
        try:
            with speech_recognition.Microphone() as mic:
                # recognizer.energy_threshold = 150
                # recognizer.pause_threshold = 0.8
                recognizer.adjust_for_ambient_noise(mic, duration=0.5)
                audio = recognizer.listen(mic)
                # print(recognizer.energy_threshold)

                self.text = recognizer.recognize_google(audio, language="ro-RO")
                self.text = self.text.lower()

                print(f"Recognized text:\n\n{self.text}")
                self.command = get_command(self.text)
                # print(f"\n\nCommand:{self.command}")

        except speech_recognition.UnknownValueError:
            recognizer = speech_recognition.Recognizer()
            # continue
