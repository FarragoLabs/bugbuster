package io.farragolabs.bugbuster.route.mode;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditMode implements Mode {
    private final File file1;

    public EditMode(File file1) {
        this.file1 = file1;
    }

    @Override
    public String body() throws IOException, JSONException {
        JSONObject bug = new JSONObject(FileUtils.readFileToString(file1));

        JSONArray rawTags = bug.getJSONArray("tags");
        List<String> tags = Lists.newArrayList();

        for (int i = 0; i < rawTags.length(); i++) {
            tags.add(rawTags.getString(i));
        }

        Map<String, String> variables = new HashMap<>();

        variables.put("title", bug.getString("title"));
        variables.put("path", file1.getAbsolutePath());
        variables.put("body", bug.getString("body"));
        variables.put("tags", StringUtils.join(tags.toArray(new String[tags.size()]), " "));

        //Dependency injection
        return new HandlebarsTemplateEngine().render(new ModelAndView(variables, "editable.hbs"));
    }
}
