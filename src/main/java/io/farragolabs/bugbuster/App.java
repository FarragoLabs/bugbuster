package io.farragolabs.bugbuster;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.File;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class App implements Route {
    public Object handle(Request request, Response response) {
        final String appname = URLDecoder.decode(request.params("appname"));
        Collection<String> transform = Collections2
                .transform(
                        Lists.newArrayList(
                                new File(BugListConfigurationModel.BUG_BUSTER_HOME + "/"
                                        + appname)
                                        .list()), new Function<String, String>() {
                            public String apply(String s) {
                                return appname + "-" + s.replaceAll(".json", "");
                            }
                        });

        String body = "";

        for (String collection : transform) {
            body = body + "<a href=\"/v1/bug/" + collection + "\">" + collection + "</a><br />";
        }

        HandlebarsTemplateEngine handlebarsTemplateEngine = new HandlebarsTemplateEngine();
        Map<String,String> parameters = new HashMap<>();
        parameters.put("appname",appname);
        parameters.put("content",body);
        return handlebarsTemplateEngine.render(new ModelAndView(parameters,"buglist.hbs"));
    }
}
