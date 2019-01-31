package helpers.api.mailchimp.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jtremeaux
 */
public class Member {
    public String emailAddress;

    public Map<String, String> mergeFields = new HashMap<>();

    public String language;

    public Member(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
