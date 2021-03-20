package plita.me.exoplayerhlswithcustomtspath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.ResolvingDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import static com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON;
import static com.google.android.exoplayer2.DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER;
import static com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory.FLAG_ALLOW_NON_IDR_KEYFRAMES;
import static com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory.FLAG_DETECT_ACCESS_UNITS;
import static com.google.android.exoplayer2.util.Util.getUserAgent;

public class PlayerActivity extends AppCompatActivity {
    PlayerView playerView;
    String TOKEN;
    String FILENAME;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        playerView = findViewById(R.id.player_view);

        Intent intent = getIntent();
        TOKEN = intent.getStringExtra("token");
        FILENAME = intent.getStringExtra("filename");

        Uri uri = Uri.parse(getString(R.string.server_address)+getString(R.string.server_streaming)+"?token="+TOKEN+"&filename="+FILENAME);
        MediaItem mediaItem=MediaItem.fromUri(uri);

        HlsMediaSource hlsMediaSource =
                new HlsMediaSource.Factory(
                        new ResolvingDataSource.Factory(
                                new DefaultHttpDataSource.Factory(),
                                (DataSpec dataSpec) -> dataSpec.withUri(resolveUri(dataSpec.uri))))
                        .createMediaSource(mediaItem);

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        player.setMediaSource(hlsMediaSource);
        player.prepare();
        playerView.setPlayer(player);
    }

    private Uri resolveUri(Uri uri) {
        if (uri.toString().contains(FILENAME)) {
            String resultPath = uri.toString();
            return Uri.parse(resultPath);
        } else {
            String resultPath=getString(R.string.server_address)+getString(R.string.server_streaming)+"?token="+TOKEN+"&filename="+uri.toString().replace(getString(R.string.server_address),"");
            return Uri.parse(resultPath);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
}