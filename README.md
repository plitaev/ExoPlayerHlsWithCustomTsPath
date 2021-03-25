# ExoPlayerHlsWithCustomTsPath
My ExoPlayer build with custom path to .ts files in HLS stream

Once I ran into a problem when I needed to play the HLS live stream from the server after authorization, passing a token in GET request.

After authorization, I received the token and made a request to .m3u8 playlist through URL with token in ExoPlayer (example: https://SERVER_ADDRESS/streaming?token=TOKEN&filename=playlist.m3u8)

So, the ExoPlayer was getting .m3u8 playlist from URL with dynamic params. Then, as I thought, the ExoPlayer would read the .ts chunks names from .m3u8 playlist and will generate GET query with prevous URL format like https://SERVER_ADDRESS/streaming?token=TOKEN&filename=fragment1.ts
  
Instead of this ExoPlayer tried to get .ts chunks from URL like https://SERVER_ADDRESS/fragment_1.ts without params in URL and got 404 error

By default, ExoPlayer will generate this URL format into chunks according to the HLS standard

This example shows how to customize ExoPlayer to use the custom URL format of .ts chunk query with the ability to transfer any parameters in the request

See https://github.com/plitaev/ExoPlayerHlsWithCustomTsPath/blob/master/app/src/main/java/plita/me/exoplayerhlswithcustomtspath/PlayerActivity.java for config details
