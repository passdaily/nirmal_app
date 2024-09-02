package info.passdaily.nirmala_convent_app.lib.dashed_circle_progress.painter;

import android.graphics.Canvas;

public interface Painter {

    void draw(Canvas canvas);

    void setColor(int color);

    int getColor();

    void onSizeChanged(int height, int width);
}