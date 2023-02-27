import os

import spotipy as sp
from dotenv import load_dotenv
from spotipy.oauth2 import SpotifyOAuth

load_dotenv()


class InvalidSearchError(Exception):
    pass


scope = 'app-remote-control user-modify-playback-state user-read-playback-state playlist-read-private'


class SpotifyClient:
    def __init__(self):
        # self.api_token = api_token
        # self.sp = sp.Spotify(auth=api_token)
        self.client_id = os.getenv("CLIENT_ID")
        self.client_secret = os.getenv("CLIENT_SECRET")
        self.redirect_uri = "http://localhost:8888/callback"
        self.scope = scope
        self.username = os.getenv("SPOTIFY_USERNAME")

        self.auth_manager = SpotifyOAuth(
            client_id=self.client_id,
            client_secret=self.client_secret,
            redirect_uri=self.redirect_uri,
            scope=self.scope
        )
        self.sp = sp.Spotify(auth_manager=self.auth_manager)

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
    spot = SpotifyClient()
    song_uri = spot.get_track_uri('Engel')
    spot.play_track(song_uri)


if __name__ == "__main__":
    main()
