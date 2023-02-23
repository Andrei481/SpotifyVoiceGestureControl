import cv2

from lib.hand_tracking.hand_tracking_module import HandDetector

def main():
    # print("Hello World")
    hands = HandDetector()
    cap = cv2.VideoCapture(0)
    previous_time = current_time = 0
    hands = HandDetector()

    while True:
        success, img = cap.read()
        img = cv2.flip(img, 1)  # careful about this and cx, cy -> they are inversed
        img = hands.find_hands(img)
        img.flags.writeable = False
        hands.get_landmarks_list(img)
        cv2.imshow("Image", img)
        if cv2.waitKey(1) & 0xFF == ord('q'):  # Press 'q' to exit
            break
    cap.release()
    cv2.destroyAllWindows()

if __name__ == "__main__":
    main()