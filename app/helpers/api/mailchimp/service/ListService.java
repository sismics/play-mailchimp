package helpers.api.mailchimp.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import helpers.api.mailchimp.MailChimp;
import helpers.api.mailchimp.model.Member;
import okhttp3.Request;
import okhttp3.RequestBody;
import play.Logger;

import java.util.Map;

/**
 * @author jtremeaux
 */
public class ListService {
    public MailChimp mailChimp;

    public ListService(MailChimp mailChimp) {
        this.mailChimp = mailChimp;
    }

    /**
     * Add a member to a list.
     *
     * @param member The member to add
     */
    public void addSubscriber(Member member) {
        JsonObject data = new JsonObject();
        data.addProperty("email_address", member.emailAddress);
        data.addProperty("status", "subscribed");
        if (!member.mergeFields.isEmpty()) {
            JsonObject mergeFields = new JsonObject();
            for (Map.Entry<String, String> entry : member.mergeFields.entrySet()) {
                mergeFields.addProperty(entry.getKey(), entry.getValue());
            }
            data.add("merge_fields", mergeFields);
        }
        if (member.language != null) {
            data.addProperty("language", member.language);
        }

        Request request = mailChimp.authenticate(new Request.Builder()
                .url("https://" + mailChimp.getDc() + ".api.mailchimp.com/3.0/lists/" + mailChimp.getListId() + "/members")
                .post(RequestBody.create(mailChimp.JSON, data.toString()))
                .build());
        mailChimp.execute(request,
                (response) -> {
                    Logger.info("MailChimp: added user: " + member.emailAddress + " to list: " + mailChimp.getListId());
                    return null;
                }, (response) -> {
                    Logger.error("Error adding user to MailChimp, response was: " + response.body().string());
                });
    }

    /**
     * Add or update a member to a list.
     *
     * @param member The member to add
     */
    public void addOrUpdateSubscriber(Member member) {
        JsonObject data = new JsonObject();
        data.addProperty("email_address", member.emailAddress);
        data.addProperty("status", "subscribed");
        if (!member.mergeFields.isEmpty()) {
            JsonObject mergeFields = new JsonObject();
            for (Map.Entry<String, String> entry : member.mergeFields.entrySet()) {
                mergeFields.addProperty(entry.getKey(), entry.getValue());
            }
            data.add("merge_fields", mergeFields);
        }
        if (member.language != null) {
            data.addProperty("language", member.language);
        }

        Request request = mailChimp.authenticate(new Request.Builder()
                .url("https://" + mailChimp.getDc() + ".api.mailchimp.com/3.0/lists/" + mailChimp.getListId() + "/members/" + member.subscriberHash())
                .put(RequestBody.create(mailChimp.JSON, data.toString()))
                .build());
        mailChimp.execute(request,
                (response) -> {
                    Logger.info("MailChimp: added or updated user: " + member.emailAddress + " to list: " + mailChimp.getListId());
                    return null;
                }, (response) -> {
                    Logger.error("Error adding or updating user to MailChimp, response was: " + response.body().string());
                });
    }

    /**
     * Manage member tags.
     *
     * @param member The member to update
     * @param tags The tags to add / remove
     */
    public void updateMemberTag(Member member, Map<String, Boolean> tags) {
        JsonObject data = new JsonObject();
        JsonArray tagsJson = new JsonArray();
        for (Map.Entry<String, Boolean> tag : tags.entrySet()) {
            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("name", tag.getKey());
            tagJson.addProperty("status", tag.getValue() ? "active" : "inactive");
            tagsJson.add(tagJson);
        }
        data.add("tags", tagsJson);
        Request request = mailChimp.authenticate(new Request.Builder()
                .url("https://" + mailChimp.getDc() + ".api.mailchimp.com/3.0/lists/" + mailChimp.getListId() + "/members/" + member.subscriberHash() + "/tags")
                .post(RequestBody.create(mailChimp.JSON, data.toString()))
                .build());
        mailChimp.execute(request,
                (response) -> {
                    Logger.info("MailChimp: added or deleted tags for user: " + member.emailAddress + ", tags: " + tags.toString());
                    return null;
                }, (response) -> {
                    Logger.error("Error adding or deleting tags for user: " + member.emailAddress + ", response: " + response.body().string());
                });
    }
}
