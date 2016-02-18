package io.farragolabs.bugbuster.route.mode;

import io.farragolabs.bugbuster.route.runnable.WriterRunnable;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;


public class CloseMode implements Mode {
    private final File file;
    private final ExecutorService executorService;
    private Response response;

    public CloseMode(File file, ExecutorService executorService, Response response) {
        this.file = file;
        this.executorService = executorService;
        this.response = response;
    }

    @Override
    public String body() throws JSONException, IOException {
        String data = new JSONObject(FileUtils.readFileToString(file))
                .put("open", false)
                .put("modified_at", System.currentTimeMillis()).toString();

        executorService.submit(new WriterRunnable(file, data));

        String[] split = file.getAbsolutePath().split("/");
        response.redirect("/v1/bug/" + split[split.length - 2] + "-" + split[split.length - 1].replaceAll(".json", ""));
        return data;
    }
}
