package io.farragolabs.bugbuster.route;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

public class BugSave implements Route {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object handle(Request request, Response response) {

        String id = request.queryParams("id");

        try {
            File file = new File(id);
            JSONObject object = new JSONObject(FileUtils.readFileToString(file));

            object.put("title", request.queryParams("title"));
            object.put("body", request.queryParams("body"));
            String tags = request.queryParams("tags");

            JSONArray jsonArray = new JSONArray();

            for (String tag : tags.split(" ")) {
                jsonArray.put(tag);
            }
            object.put("tags", jsonArray);
            object.put("modified_at", System.currentTimeMillis());

            FileUtils.writeStringToFile(file, object.toString());

            logger.info("Saving bug with id " + id);

            String[] split = file.getParent().split("/");
            System.out.println();

            response.redirect("/v1/bug/" + split[split.length - 1] + "-" + file.getName().replaceAll(".json", ""));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "404";
        }

        return null;
    }
}
