package com.example.oauthlogin.profile;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/** Student task: map GitHub attributes into an application profile. */
@Service
public class GitHubProfileService {
    public GitHubProfile from(OAuth2User user) {
        // TODO: read numeric "id", required "login", and optional "name" attributes.
        // Use login as displayName when name is missing or blank.
        // GitHub id is stable; a login name may change.
        // Never include the GitHub access token in the profile.
        Object idAttribute = user.getAttribute("id");
        if (!(idAttribute instanceof Number id)) {
            throw new IllegalArgumentException("GitHub profile is missing a numeric id");
        }

        String login = user.getAttribute("login");
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("GitHub profile is missing a login");
        }

        String name = user.getAttribute("name");
        String displayName = (name == null || name.isBlank()) ? login : name;

        return new GitHubProfile(id.longValue(), login, displayName);
    }
}
