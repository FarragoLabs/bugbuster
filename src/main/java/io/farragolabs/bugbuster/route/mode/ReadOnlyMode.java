package io.farragolabs.bugbuster.route.mode;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.farragolabs.bugbuster.PageUtils.HEADER;
import static io.farragolabs.bugbuster.PageUtils.bug;
import static io.farragolabs.bugbuster.PageUtils.markDownToHtml;

public class ReadOnlyMode implements Mode {
    private final File file1;
    private final Request request;

    public ReadOnlyMode(File file1, Request request) {

        this.file1 = file1;
        this.request = request;
    }

    @Override
    public String body() throws JSONException, IOException {
        JSONObject bug = new JSONObject(FileUtils.readFileToString(file1));

        JSONArray rawTags = bug.getJSONArray("tags");
        List<String> tags = Lists.newArrayList();

        for (int i = 0; i < rawTags.length(); i++) {
            tags.add(rawTags.getString(i));
        }

        String body = markDownToHtml(bug.getString("body"));

        String bug1 = bug(file1,
                request.url(),
                bug.getString("title"),
                bug.getBoolean("open"),
                body,
                tags.toArray(new String[tags.size()]));

        Map<String, Object> params = new HashMap<>();

        if(bug.has("comments"))
        {
            JSONArray comments = bug.getJSONArray("comments");

            List<String>commmetz = Lists.newArrayList();

            for(int i=0; i < comments.length(); i++)
            {
                commmetz.add(comments.getString(i));
            }
            params.put("comments", commmetz);
        }
        params.put("content", bug1);
        params.put("appname",request.params("bugid").split("-")[0]);
        params.put("path",file1.getAbsolutePath());

        HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();
        return handlebarsTemplateEngine.render(new ModelAndView(params, "bug.hbs"));
    }
}
