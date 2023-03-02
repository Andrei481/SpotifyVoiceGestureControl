import os
import time

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
        self.devices = self.sp.devices()

    def check_active_devices(self):
        if not self.devices:
            print("No active devices")
            return None
        return self.devices["devices"][0]["id"]

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

    def play(self, uri=None):
        device_id = self.check_active_devices()
        self.sp.start_playback(uris=[uri], device_id=device_id)

    def get_playlist_id(self, name: str):
        playlists = self.sp.user_playlists(user=self.username)
        while playlists:
            for playlist in playlists['items']:
                if name == str(playlist['name']).lower():
                    return playlist['id']
            if playlists['next']:
                playlists = sp.next(playlists)
            else:
                playlists = None
        return None

    def play_playlist(self, uri=None):
        device_id = self.check_active_devices()
        self.sp.start_playback(context_uri=f"spotify:playlist:{uri}", device_id=device_id)

    def pause(self):
        current_track = self.sp.currently_playing()
        if current_track['is_playing']:
            self.sp.pause_playback()
        else:
            print('Already paused')

    def resume_playing(self):
        current_track = self.sp.currently_playing()
        if current_track is None:
            print("No track is currently playing")
        elif current_track['is_playing']:
            print('Already playing')
        else:
            device_id = self.check_active_devices()
            self.sp.start_playback(device_id=device_id)
            print("Resumed playback")

    def skip_song(self):
        self.sp.next_track()
        current_track = self.sp.currently_playing()
        if current_track is None:
            print("No track left in queue")
        elif current_track['is_playing']:
            print('Skipped')

    def previous_song(self):
        self.sp.previous_track()
        current_track = self.sp.currently_playing()
        if current_track is None:
            print("No previous track in queue")
        elif current_track['is_playing']:
            print('Previous')

    def set_volume(self, increase: bool):
        """

        :param increase: True - increase volume ; False - decrease volume
        """
        current_volume = self.sp.current_playback()['device']['volume_percent']
        if increase:
            new_volume = min(current_volume + 10, 100)
            self.sp.volume(volume_percent=new_volume)
        else:
            new_volume = min(current_volume - 10, 100)
            self.sp.volume(volume_percent=new_volume)


def main():
    # pass
    spot = SpotifyClient()
    # song_uri = spot.get_track_uri('The Perfect Girl')
    playlist_uri = spot.get_playlist_id('based')
    spot.play_playlist(playlist_uri)


if __name__ == "__main__":
    main()
