package info.passdaily.nirmala_convent_app.services.downloader.internal;

import info.passdaily.nirmala_convent_app.services.downloader.Response;
import info.passdaily.nirmala_convent_app.services.downloader.request.DownloadRequest;

public class SynchronousCall {

    public final DownloadRequest request;

    public SynchronousCall(DownloadRequest request) {
        this.request = request;
    }

    public Response execute() {
        DownloadTask downloadTask = DownloadTask.create(request);
        return downloadTask.run();
    }

}
