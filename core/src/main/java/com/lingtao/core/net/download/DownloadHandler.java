package com.lingtao.core.net.download;

import android.os.AsyncTask;

import com.lingtao.core.net.RestCreator;
import com.lingtao.core.net.callback.IError;
import com.lingtao.core.net.callback.IFailure;
import com.lingtao.core.net.callback.IRequest;
import com.lingtao.core.net.callback.ISuccess;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadHandler {
    private final HashMap<String,Object> PARAMS;
    private final String URL;
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String FILENAME;

    public DownloadHandler(HashMap<String, Object> params, String url,
                           IRequest request, ISuccess success,
                           IFailure failure, IError error,
                           String downloadDir, String extension, String filename) {
        this.PARAMS = params;
        this.URL = url;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.FILENAME = filename;
    }

    public final void handleDownload(){
        Call<ResponseBody> call = RestCreator.getRestService().download(URL, PARAMS);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                            DOWNLOAD_DIR, EXTENSION, response.body(), FILENAME);
                    //如果下载完成
                    if (task.isCancelled()) {
                        if (REQUEST != null) {
                            REQUEST.onRequestEnd();
                        }
                    }
                } else {
                    if (ERROR != null) {
                        ERROR.onError(response.code(), response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(FAILURE!=null){
                    FAILURE.onFailure();
                }
            }
        });
    }
}
