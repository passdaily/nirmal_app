package info.passdaily.nirmala_convent_app.lib.dashed_circle_progress.painter;

public interface ProgressPainter extends Painter {

    void setMax(float max);

    void setMin(float min);

    void setValue(float value);

}
