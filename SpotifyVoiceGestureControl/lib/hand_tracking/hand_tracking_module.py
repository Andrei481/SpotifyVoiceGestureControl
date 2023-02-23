import cv2
import mediapipe as mp
import time


class HandDetector:
    def __init__(self, static_image_mode=False, max_num_hands=2, model_complexity=1, detection_confidence=0.5,
                 tracking_confidence=0.5):
        self.static_image_mode = static_image_mode
        self.max_num_hands = max_num_hands
        self.model_complexity = model_complexity
        self.detection_confidence = detection_confidence
        self.tracking_confidence = tracking_confidence

        self.mp_hands = mp.solutions.hands
        self.hands = self.mp_hands.Hands(self.static_image_mode, self.max_num_hands, self.model_complexity,
                                         self.detection_confidence, self.tracking_confidence)
        self.mp_drawing = mp.solutions.drawing_utils

    def find_hands(self, img, draw=True):
        imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        self.results = self.hands.process(imgRGB)
        self.results_landmarks = self.results.multi_hand_landmarks
        if draw:
            if self.results_landmarks:
                for hand_landmark in self.results_landmarks:
                    self.mp_drawing.draw_landmarks(img, hand_landmark, self.mp_hands.HAND_CONNECTIONS)
        return img

    def get_landmarks_list(self, img, hand_number=0, draw=True):
        landmarks_list = []
        if self.results_landmarks:
            my_hand = self.results_landmarks[hand_number]
            for id, landmark in enumerate(my_hand.landmark):
                h, w, c = img.shape
                cx, cy = int(landmark.x * w), int(landmark.y * h)
                print([id, cx, cy])
                landmarks_list.append([id, cx, cy])
                if draw:
                    cv2.circle(img, (cx, cy), 7, (0, 255, 255), cv2.FILLED)
        return landmarks_list
