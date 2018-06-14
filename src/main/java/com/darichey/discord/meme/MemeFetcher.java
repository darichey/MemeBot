package com.darichey.discord.meme;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemeFetcher {

    private final DefaultPaginator<Submission> paginator;
    private final Mono<Submission> nextMeme;
    private final int resetTime;

    private final Queue<Submission> queue = new LinkedList<>();
    private final AtomicLong lastRequestTime = new AtomicLong();

    MemeFetcher(String clientId, String secret, String uuid, Set<String> subreddits, String userAgent, int resetTime) {
        Credentials credentials = Credentials.userless(clientId, secret, UUID.fromString(uuid));
        NetworkAdapter networkAdapter = new OkHttpNetworkAdapter(new UserAgent(userAgent));
        RedditClient redditClient = OAuthHelper.automatic(networkAdapter, credentials);

        String subredditsString = subreddits.stream().collect(Collectors.joining("+"));

        this.paginator = redditClient.subreddit(subredditsString).posts()
                .sorting(SubredditSort.HOT)
                .build();

        this.nextMeme = Mono.defer(() ->
            Mono.fromRunnable(() -> {
                if (lastRequestTime.get() == 0) {
                    lastRequestTime.set(System.currentTimeMillis());
                } else if (shouldReset()) {
                    reset();
                }

                if (queue.isEmpty()) {
                    for (Submission submission : paginator.next()) {
                        if (!submission.isSelfPost()) {
                            queue.add(submission);
                        }
                    }

                    lastRequestTime.set(System.currentTimeMillis());
                }
            }).then(Mono.fromSupplier(queue::remove))
        );

        this.resetTime = resetTime;
    }

    public Mono<Submission> nextMeme() {
        return nextMeme;
    }

    public void reset() {
        queue.clear();
        paginator.restart();
    }

    private boolean shouldReset() {
        return Instant.now().isAfter(Instant.ofEpochMilli(lastRequestTime.get()).plusSeconds(resetTime));
    }
}
