import spotipy
import os
from dotenv import load_dotenv

load_dotenv()


class InvalidSearchError(Exception):
    pass


spotify_auth_token = os.getenv("SPOTIFY_AUTH_TOKEN")
print(spotify_auth_token)


class SpotifyClient:
    def __init__(self, api_token):
        self.api_token = api_token
        self.sp = spotipy.Spotify(auth=api_token)

    def get_track_uri(self, name: str) -> str:
        """
        :param name: track name
        :return: Spotify uri of the desired track
        """

        # Replace all spaces in name with '+'
        original = name
        name = name.replace(' ', '+')

        results = self.sp.search(q=name, limit=1, type='track')
        if not results['tracks']['items']:
            raise InvalidSearchError(f'No track named "{original}"')
        track_uri = results['tracks']['items'][0]['uri']
        return track_uri

    def play_track(self, uri=None):
        self.sp.start_playback(uris=[uri])


def main():
    # pass
    sp = SpotifyClient(spotify_auth_token)
    song_uri = sp.get_track_uri('Dragostea din tei')
    sp.play_track(song_uri)


if __name__ == "__main__":
    main()
