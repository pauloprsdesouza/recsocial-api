// package br.com.api.controllers;

// import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItem;
// import br.com.api.infrastructure.database.datamodel.recommendations.Items.RecommendationItemPK;
// import br.com.api.infrastructure.database.datamodel.recommendations.Recommendation;
// import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationRepository;
// import br.com.api.infrastructure.database.datamodel.recommendations.RecommendationTypeEnum;
// import br.com.api.infrastructure.database.datamodel.tweets.Tweet;
// import br.com.api.infrastructure.database.datamodel.tweets.TweetRepository;
// import br.com.api.infrastructure.database.datamodel.usersaccount.UserAccount;
// import br.com.api.infrastructure.services.RecommendationService;
// import com.google.gson.JsonObject;
// import java.util.Comparator;
// import java.util.Date;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.LinkedList;
// import java.util.List;
// import java.util.Map;
// import java.util.Map.Entry;
// import java.util.Set;
// import java.util.stream.Collectors;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;


// @RestController
// @RequestMapping("/recommendations")
// public class RecommendationController {

// private RecommendationService _recService;
// private TweetRepository _tweets;
// private RecommendationRepository _recommendations;

// public RecommendationController(RecommendationService recService, TweetRepository tweets,
// RecommendationRepository recommendations) {
// _recService = recService;
// _tweets = tweets;
// _recommendations = recommendations;
// }

// @PostMapping("/generate-recommendations")
// public ResponseEntity<String> generateRecommendations(
// @RequestBody TagRecommendationViewModel viewModel) {
// UserAccount user = HttpContext.getUserAccount();

// Map<Tweet, Double> tweetsScore;
// List<Tweet> tweets;

// if (viewModel.getRecommendationType() == RecommendationTypeEnum.SocialCapial || viewModel
// .getRecommendationType() == RecommendationTypeEnum.SocialCapitalSentiment) {

// tweets = _tweets.getTweetsNotRecommendedBySocialCapital(user.getId(),
// viewModel.getIdsEntities());

// tweetsScore = new HashMap<>();
// } else {
// tweets = _tweets.getTweetsNotRecommended(user.getId(), viewModel.getIdsEntities());

// if (viewModel.getRecommendationType() == RecommendationTypeEnum.CosineSimilarity) {
// tweetsScore = _recService.setActiveUser(user).setTweetsByEntity(tweets)
// .calculateCosineSimilarityScores();
// } else {
// tweetsScore = _recService.setActiveUser(user).setTweetsByEntity(tweets)
// .calculateBaseline01Scores();
// }
// }

// for (Tweet tweet : tweets) {
// tweetsScore.put(tweet, tweet.getScsaScore());
// }

// Recommendation recommendation = new Recommendation();
// recommendation.setUser(user);
// recommendation.setRegistrationDate(new Date());
// recommendation.setRecommendationType(viewModel.getRecommendationType());

// recommendation = _recommendations.save(recommendation);

// Set<RecommendationItem> recommendedItems = new HashSet<>();

// for (Entry<Tweet, Double> tweet : tweetsScore.entrySet()) {
// RecommendationItemPK itemPK = new RecommendationItemPK();
// itemPK.setIdTweet(tweet.getKey().getId());
// itemPK.setIdRecommendation(recommendation.getId());

// RecommendationItem item = new RecommendationItem();
// item.setScore(tweet.getValue());
// item.setId(itemPK);

// recommendedItems.add(item);
// }

// LinkedList<RecommendationItem> recommendedRankedItems = new LinkedList<>(recommendedItems
// .stream().sorted(Comparator.comparing(RecommendationItem::getScore).reversed())
// .limit(10).collect(Collectors.toCollection(LinkedList::new)));

// int i = 1;
// for (RecommendationItem item : recommendedRankedItems) {
// item.setRank(i++);
// }

// // recommendation.setItems(recommendedRankedItems);

// _recommendations.save(recommendation);

// return ResponseEntity.noContent().build();
// }

// @PostMapping("/update-rating")
// public ResponseEntity<String> updateRating(
// @RequestBody UpdateRatingRecommendationViewModel viewModel) {
// RecommendationItem recommendationItem = _recommendations
// .withIdRecommendation(viewModel.getIdRecommendation(), viewModel.getIdTweet());

// if (recommendationItem != null) {
// recommendationItem.setRating(viewModel.getRating());
// if (viewModel.getRating() == null) {
// recommendationItem.setRegistrationRating(null);
// } else {
// recommendationItem.setRegistrationRating(new Date());
// }
// _recommendations.save(recommendationItem);
// } else {
// return MessageJson.error("Não foi possível encontrar a recomendação solicitada.");
// }

// return ResponseEntity.noContent().build();
// }

// @PostMapping("/not-evaluated")
// public ResponseEntity<String> notEvaluated(@RequestBody RecommendationTypeViewModel viewModel) {
// UserAccount user = HttpContext.getUserAccount();

// List<RecommendationItem> recommendations =
// _recommendations.whithUserAndRecommendationsNotEvaluated(user.getId(),
// viewModel.getRecommendationType());

// JsonObject resultJson = new JsonObject();
// resultJson.add("recommendations", RecommendationItemJson.toJson(recommendations));

// return new ResponseEntity<String>(resultJson.toString(), HttpStatus.OK);
// }

// @PostMapping("/finished-evaluations")
// public ResponseEntity<String> finishedEvaluations() {
// UserAccount user = HttpContext.getUserAccount();

// List<Recommendation> recommendations = _recommendations.withIdUserToFinish(user.getId());

// for (Recommendation recommendation : recommendations) {
// recommendation.setFinishedDate(new Date());
// }

// _recommendations.saveAll(recommendations);

// return ResponseEntity.noContent().build();
// }
// }
