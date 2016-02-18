package io.farragolabs.bugbuster.route;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.farragolabs.bugbuster.BugListConfigurationModel;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AppList implements Route {
    private HandlebarsTemplateEngine handlebarsTemplateEngine;

    public AppList(HandlebarsTemplateEngine handlebarsTemplateEngine) {
        this.handlebarsTemplateEngine = handlebarsTemplateEngine;
    }

    public Object handle(Request request, Response response) {
        List<String> apps = Lists.newArrayList(BugListConfigurationModel.BUG_BUSTER_DIR.list());
        Collections.sort(apps);
        HashMap<String, Object> appList = Maps.newHashMap();
        appList.put("applist", apps);
        appList.put("home", "/");

        return handlebarsTemplateEngine.render(new ModelAndView(appList, "app.hbs"));
    }
}
