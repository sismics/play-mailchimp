package helpers.api.mailchimp.mock;

import helpers.api.mailchimp.service.ListService;

import static org.mockito.Mockito.mock;

/**
 * @author jtremeaux
 */
public class MockListService {
    /**
     * Create a mock of ListService.
     *
     * @return The mock
     */
    public static ListService create() {
        ListService listService = mock(ListService.class);
        return listService;
    }
}
