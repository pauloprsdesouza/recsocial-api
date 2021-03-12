package br.com.api.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweet;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetPK;
import br.com.api.infrastructure.database.datamodel.entitiestweet.EntityTweetRepository;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUser;
import br.com.api.infrastructure.database.datamodel.twitterusers.TwitterUserRepository;
import br.com.api.infrastructure.services.TwitterV2Service;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

        @Autowired
        private TwitterV2Service _twitterService;

        @Autowired
        private EntityTweetRepository _entities;

        @Autowired
        private TwitterUserRepository _twitterUsers;

        @GetMapping("/extract-data")
        public ResponseEntity<?> extractData() throws Exception {
                Map<String, EntityTweetPK> accounts = new HashMap<>();

                // accounts.put("u2", new EntityTweetPK(1L, 1L));
                // accounts.put("BonJovi", new EntityTweetPK(1L, 1L));
                // accounts.put("pinkfloyd", new EntityTweetPK(1L, 1L));
                // accounts.put("acdc", new EntityTweetPK(1L, 1L));
                // accounts.put("gunsnroses", new EntityTweetPK(1L, 1L));
                // accounts.put("linkinpark", new EntityTweetPK(1L, 1L));
                // accounts.put("evanescence", new EntityTweetPK(1L, 1L));
                // accounts.put("scorpions", new EntityTweetPK(1L, 1L));
                // accounts.put("PearlJam", new EntityTweetPK(1L, 1L));
                // accounts.put("ChiliPeppers", new EntityTweetPK(1L, 1L));

                // accounts.put("taylorswift13", new EntityTweetPK(2L, 1L));
                // accounts.put("selenagomez", new EntityTweetPK(2L, 1L));
                // accounts.put("rihanna", new EntityTweetPK(2L, 1L));
                // accounts.put("ladygaga", new EntityTweetPK(2L, 1L));
                // accounts.put("jtimberlake", new EntityTweetPK(2L, 1L));
                // accounts.put("katyperry", new EntityTweetPK(2L, 1L));
                // accounts.put("KimKardashian", new EntityTweetPK(2L, 1L));
                // accounts.put("BrunoMars", new EntityTweetPK(2L, 1L));
                // accounts.put("johnlegend", new EntityTweetPK(2L, 1L));
                // accounts.put("ArianaGrande", new EntityTweetPK(2L, 1L));

                // accounts.put("thelittleidiot", new EntityTweetPK(3L, 1L));
                // accounts.put("R3HAB", new EntityTweetPK(3L, 1L));
                // accounts.put("steveaoki", new EntityTweetPK(3L, 1L));
                // accounts.put("davidguetta", new EntityTweetPK(3L, 1L));
                // accounts.put("QUINTINOO", new EntityTweetPK(3L, 1L));
                // accounts.put("arminvanbuuren", new EntityTweetPK(3L, 1L));
                // accounts.put("bobsinclar", new EntityTweetPK(3L, 1L));
                // accounts.put("DonDiablo", new EntityTweetPK(3L, 1L));
                // accounts.put("afrojack", new EntityTweetPK(3L, 1L));
                // accounts.put("Zedd", new EntityTweetPK(3L, 1L));

                // accounts.put("Korn", new EntityTweetPK(4L, 1L));
                // accounts.put("systemofadown", new EntityTweetPK(4L, 1L));
                // accounts.put("Slayer", new EntityTweetPK(4L, 1L));
                // accounts.put("slipknot", new EntityTweetPK(4L, 1L));
                // accounts.put("BlackSabbath", new EntityTweetPK(4L, 1L));
                // accounts.put("Metallica", new EntityTweetPK(4L, 1L));
                // accounts.put("IronMaiden", new EntityTweetPK(4L, 1L));
                // accounts.put("TheOfficialA7X", new EntityTweetPK(4L, 1L));
                // accounts.put("myMotorhead", new EntityTweetPK(4L, 1L));
                // accounts.put("judaspriest", new EntityTweetPK(4L, 1L));

                // accounts.put("50cent", new EntityTweetPK(5L, 1L));
                // accounts.put("icecube", new EntityTweetPK(5L, 1L));
                // accounts.put("LilTunechi", new EntityTweetPK(5L, 1L));
                // accounts.put("wizkhalifa", new EntityTweetPK(5L, 1L));
                // accounts.put("pharoahemonch", new EntityTweetPK(5L, 1L));
                // accounts.put("Drake", new EntityTweetPK(5L, 1L));
                // accounts.put("JColeNC", new EntityTweetPK(5L, 1L));
                // accounts.put("SnoopDogg", new EntityTweetPK(5L, 1L));
                // accounts.put("Nas", new EntityTweetPK(5L, 1L));
                // accounts.put("Eminem", new EntityTweetPK(5L, 1L));

                // accounts.put("Xbox", new EntityTweetPK(6L, 2L));
                // accounts.put("PlayStation", new EntityTweetPK(6L, 2L));
                // accounts.put("RE_Games", new EntityTweetPK(6L, 2L));
                // accounts.put("CyberpunkGame", new EntityTweetPK(6L, 2L));
                // accounts.put("Activision", new EntityTweetPK(6L, 2L));
                // accounts.put("RockstarGames", new EntityTweetPK(6L, 2L));
                // accounts.put("EA", new EntityTweetPK(6L, 2L));
                // accounts.put("Blizzard_Ent", new EntityTweetPK(6L, 2L));
                // accounts.put("valvesoftware", new EntityTweetPK(6L, 2L));
                // accounts.put("DOOM", new EntityTweetPK(6L, 2L));

                // accounts.put("bridgerton", new EntityTweetPK(7L, 2L));
                // accounts.put("GameOfThrones", new EntityTweetPK(7L, 2L));
                // accounts.put("TheCrownNetflix", new EntityTweetPK(7L, 2L));
                // accounts.put("ThePeakyBlinder", new EntityTweetPK(7L, 2L));
                // accounts.put("witchernetflix", new EntityTweetPK(7L, 2L));
                // accounts.put("cw_spn", new EntityTweetPK(7L, 2L));
                // accounts.put("SHO_Billions", new EntityTweetPK(7L, 2L));
                // accounts.put("themandalorian", new EntityTweetPK(7L, 2L));
                // accounts.put("WalkingDead_AMC", new EntityTweetPK(7L, 2L));
                // accounts.put("SuitsPeacock", new EntityTweetPK(7L, 2L));

                // accounts.put("LinkedIn", new EntityTweetPK(8L, 2L));
                // accounts.put("Pinterest", new EntityTweetPK(8L, 2L));
                // accounts.put("wa_status", new EntityTweetPK(8L, 2L));
                // accounts.put("tiktok_us", new EntityTweetPK(8L, 2L));
                // accounts.put("instagram", new EntityTweetPK(8L, 2L));
                // accounts.put("Quora", new EntityTweetPK(8L, 2L));
                // accounts.put("reddit", new EntityTweetPK(8L, 2L));
                // accounts.put("telegram", new EntityTweetPK(8L, 2L));
                // accounts.put("tumblr", new EntityTweetPK(8L, 2L));
                // accounts.put("Snapchat", new EntityTweetPK(8L, 2L));

                // accounts.put("arrington", new EntityTweetPK(9L, 2L));
                // accounts.put("jkottke", new EntityTweetPK(9L, 2L));
                // accounts.put("LaurenConrad", new EntityTweetPK(9L, 2L));
                // accounts.put("OliviaPalermo", new EntityTweetPK(9L, 2L));
                // accounts.put("SincerelyJules", new EntityTweetPK(9L, 2L));
                // accounts.put("dooce", new EntityTweetPK(9L, 2L));
                // accounts.put("joannagaines", new EntityTweetPK(9L, 2L));
                // accounts.put("meghanrienks", new EntityTweetPK(9L, 2L));
                // accounts.put("randfish", new EntityTweetPK(9L, 2L));
                // accounts.put("jackieaina", new EntityTweetPK(9L, 2L));

                // accounts.put("AppleTV", new EntityTweetPK(10L, 2L));
                // accounts.put("Pixar", new EntityTweetPK(10L, 2L));
                // accounts.put("disneyplus", new EntityTweetPK(10L, 2L));
                // accounts.put("PrimeVideo", new EntityTweetPK(10L, 2L));
                // accounts.put("warnerbrostv", new EntityTweetPK(10L, 2L));
                // accounts.put("HBO", new EntityTweetPK(10L, 2L));
                // accounts.put("nbc", new EntityTweetPK(10L, 2L));
                // accounts.put("20thcentury", new EntityTweetPK(10L, 2L));
                // accounts.put("netflix", new EntityTweetPK(10L, 2L));
                // accounts.put("IMDb", new EntityTweetPK(10L, 2L));

                // accounts.put("LFC", new EntityTweetPK(11L, 4L));
                // accounts.put("FCBarcelona", new EntityTweetPK(11L, 4L));
                // accounts.put("ManUtd", new EntityTweetPK(11L, 4L));
                // accounts.put("juventusfcen", new EntityTweetPK(11L, 4L));
                // accounts.put("ChelseaFC", new EntityTweetPK(11L, 4L));
                // accounts.put("Arsenal", new EntityTweetPK(11L, 4L));
                // accounts.put("realmadriden", new EntityTweetPK(11L, 4L));
                // accounts.put("FCBayern", new EntityTweetPK(11L, 4L));
                // accounts.put("PSG_English", new EntityTweetPK(11L, 4L));
                // accounts.put("SevillaFC_ENG", new EntityTweetPK(11L, 4L));

                // accounts.put("Carlossainz55", new EntityTweetPK(12L, 4L));
                // accounts.put("danielricciardo", new EntityTweetPK(12L, 4L));
                // accounts.put("LandoNorris", new EntityTweetPK(12L, 4L));
                // accounts.put("Max33Verstappen", new EntityTweetPK(12L, 4L));
                // accounts.put("RGrosjean", new EntityTweetPK(12L, 4L));
                // accounts.put("Charles_Leclerc", new EntityTweetPK(12L, 4L));
                // accounts.put("HulkHulkenberg", new EntityTweetPK(12L, 4L));
                accounts.put("LewisHamilton", new EntityTweetPK(12L, 4L));
                // accounts.put("PierreGASLY", new EntityTweetPK(12L, 4L));
                // accounts.put("PaulDiResta", new EntityTweetPK(12L, 4L));

                accounts.put("MiamiHEAT", new EntityTweetPK(13L, 4L));
                // accounts.put("Bucks", new EntityTweetPK(13L, 4L));
                // accounts.put("chicagobulls", new EntityTweetPK(13L, 4L));
                // accounts.put("DetroitPistons", new EntityTweetPK(13L, 4L));
                // accounts.put("LAClippers", new EntityTweetPK(13L, 4L));
                // accounts.put("nyknicks", new EntityTweetPK(13L, 4L));
                // accounts.put("Lakers", new EntityTweetPK(13L, 4L));
                // accounts.put("sixers", new EntityTweetPK(13L, 4L));
                // accounts.put("HoustonRockets", new EntityTweetPK(13L, 4L));
                // accounts.put("cavs", new EntityTweetPK(13L, 4L));

                accounts.put("ufc", new EntityTweetPK(14L, 4L));
                // accounts.put("RondaRousey", new EntityTweetPK(14L, 4L));
                // accounts.put("cainmma", new EntityTweetPK(14L, 4L));
                // accounts.put("danhendo", new EntityTweetPK(14L, 4L));
                // accounts.put("ChuckLiddell", new EntityTweetPK(14L, 4L));
                // accounts.put("JonnyBones", new EntityTweetPK(14L, 4L));
                // accounts.put("DominickCruz", new EntityTweetPK(14L, 4L));
                // accounts.put("GeorgesStPierre", new EntityTweetPK(14L, 4L));
                // accounts.put("MightyMouse", new EntityTweetPK(14L, 4L));
                // accounts.put("stipemiocic", new EntityTweetPK(14L, 4L));

                // accounts.put("richie_porte", new EntityTweetPK(15L, 4L));
                // accounts.put("chrisfroome", new EntityTweetPK(15L, 4L));
                // accounts.put("Doctor_Hutch", new EntityTweetPK(15L, 4L));
                // accounts.put("MarkCavendish", new EntityTweetPK(15L, 4L));
                // accounts.put("larrywarbasse", new EntityTweetPK(15L, 4L));
                // accounts.put("simongerrans", new EntityTweetPK(15L, 4L));
                // accounts.put("daniellloyd1", new EntityTweetPK(15L, 4L));
                // accounts.put("GeraintThomas86", new EntityTweetPK(15L, 4L));
                // accounts.put("GregVanAvermaet", new EntityTweetPK(15L, 4L));
                // accounts.put("iamtedking", new EntityTweetPK(15L, 4L));

                // accounts.put("MarketWatch", new EntityTweetPK(16L, 5L));
                // accounts.put("HarvardBiz", new EntityTweetPK(16L, 5L));
                // accounts.put("CNNBusiness", new EntityTweetPK(16L, 5L));
                // accounts.put("business", new EntityTweetPK(16L, 5L));
                // accounts.put("FastCompany", new EntityTweetPK(16L, 5L));
                // accounts.put("nytimesbusiness", new EntityTweetPK(16L, 5L));
                // accounts.put("ReutersBiz", new EntityTweetPK(16L, 5L));
                // accounts.put("Entrepreneur", new EntityTweetPK(16L, 5L));
                // accounts.put("BW", new EntityTweetPK(16L, 5L));
                // accounts.put("businessinsider", new EntityTweetPK(16L, 5L));

                // accounts.put("NBCSports", new EntityTweetPK(17L, 5L));
                // accounts.put("SkySports", new EntityTweetPK(17L, 5L));
                // accounts.put("espn", new EntityTweetPK(17L, 5L));
                // accounts.put("guardian_sport", new EntityTweetPK(17L, 5L));
                // accounts.put("BBCSport", new EntityTweetPK(17L, 5L));
                // accounts.put("TimesSport", new EntityTweetPK(17L, 5L));
                // accounts.put("Sport24news", new EntityTweetPK(17L, 5L));
                // accounts.put("cnnsport", new EntityTweetPK(17L, 5L));
                // accounts.put("MailSport", new EntityTweetPK(17L, 5L));
                // accounts.put("SkySportsF1", new EntityTweetPK(17L, 5L));

                // accounts.put("latimestravel", new EntityTweetPK(18L, 5L));
                // accounts.put("TravelLeisure", new EntityTweetPK(18L, 5L));
                // accounts.put("NatGeoTravel", new EntityTweetPK(18L, 5L));
                // accounts.put("CNNTravel", new EntityTweetPK(18L, 5L));
                // accounts.put("TravelMagazine", new EntityTweetPK(18L, 5L));
                // accounts.put("lonelyplanet", new EntityTweetPK(18L, 5L));
                // accounts.put("GuardianTravel", new EntityTweetPK(18L, 5L));
                // accounts.put("IndyTravel", new EntityTweetPK(18L, 5L));
                // accounts.put("timestravel", new EntityTweetPK(18L, 5L));
                // accounts.put("usatodaytravel", new EntityTweetPK(18L, 5L));

                // accounts.put("FoxNews", new EntityTweetPK(19L, 5L));
                // accounts.put("NBCNews", new EntityTweetPK(19L, 5L));
                // accounts.put("nytimes", new EntityTweetPK(19L, 5L));
                // accounts.put("Independent", new EntityTweetPK(19L, 5L));
                // accounts.put("guardian", new EntityTweetPK(19L, 5L));
                // accounts.put("TheEconomist", new EntityTweetPK(19L, 5L));
                // accounts.put("TIME", new EntityTweetPK(19L, 5L));
                // accounts.put("CBSNews", new EntityTweetPK(19L, 5L));
                // accounts.put("ABC", new EntityTweetPK(19L, 5L));
                // accounts.put("CNN", new EntityTweetPK(19L, 5L));

                // accounts.put("BBCTech", new EntityTweetPK(20L, 5L));
                // accounts.put("WIRED", new EntityTweetPK(20L, 5L));
                // accounts.put("techreview", new EntityTweetPK(20L, 5L));
                // accounts.put("cnntech", new EntityTweetPK(20L, 5L));
                // accounts.put("guardiantech", new EntityTweetPK(20L, 5L));
                // accounts.put("TechCrunch", new EntityTweetPK(20L, 5L));
                // accounts.put("ForbesTech", new EntityTweetPK(20L, 5L));
                // accounts.put("ReutersTech", new EntityTweetPK(20L, 5L));
                // accounts.put("SkyNewsTech", new EntityTweetPK(20L, 5L));
                // accounts.put("nytimestech", new EntityTweetPK(20L, 5L));

                // accounts.put("tomhanks", new EntityTweetPK(21L, 3L));
                // accounts.put("RobertDowneyJr", new EntityTweetPK(21L, 3L));
                // accounts.put("CharlizeAfrica", new EntityTweetPK(21L, 3L));
                // accounts.put("AdamSandler", new EntityTweetPK(21L, 3L));
                // accounts.put("SamuelLJackson", new EntityTweetPK(21L, 3L));
                // accounts.put("jes_chastain", new EntityTweetPK(21L, 3L));
                // accounts.put("TomCruise", new EntityTweetPK(21L, 3L));
                // accounts.put("LeoDiCaprio", new EntityTweetPK(21L, 3L));
                // accounts.put("RealHughJackman", new EntityTweetPK(21L, 3L));
                // accounts.put("CameronDiaz", new EntityTweetPK(21L, 3L));

                // accounts.put("JimGaffigan", new EntityTweetPK(22L, 3L));
                // accounts.put("ConanOBrien", new EntityTweetPK(22L, 3L));
                // accounts.put("StephenAtHome", new EntityTweetPK(22L, 3L));
                // accounts.put("thesulk", new EntityTweetPK(22L, 3L));
                // accounts.put("sarahschauer", new EntityTweetPK(22L, 3L));
                // accounts.put("KevinHart4real", new EntityTweetPK(22L, 3L));
                // accounts.put("billburr", new EntityTweetPK(22L, 3L));
                // accounts.put("KenJennings", new EntityTweetPK(22L, 3L));
                // accounts.put("richardpryor", new EntityTweetPK(22L, 3L));
                // accounts.put("jenstatsky", new EntityTweetPK(22L, 3L));

                // accounts.put("sundarpichai", new EntityTweetPK(23L, 3L));
                // accounts.put("PGelsinger", new EntityTweetPK(23L, 3L));
                // accounts.put("BillGates", new EntityTweetPK(23L, 3L));
                // accounts.put("Benioff", new EntityTweetPK(23L, 3L));
                // accounts.put("satyanadella", new EntityTweetPK(23L, 3L));
                // accounts.put("ajassy", new EntityTweetPK(23L, 3L));
                // accounts.put("ericsyuan", new EntityTweetPK(23L, 3L));
                // accounts.put("tim_cook", new EntityTweetPK(23L, 3L));
                // accounts.put("MichaelDell", new EntityTweetPK(23L, 3L));
                // accounts.put("JeffBezos", new EntityTweetPK(23L, 3L));

                // accounts.put("Cristiano", new EntityTweetPK(24L, 3L));
                // accounts.put("HKane", new EntityTweetPK(24L, 3L));
                // accounts.put("Aubameyang7", new EntityTweetPK(24L, 3L));
                // accounts.put("AnthonyMartial", new EntityTweetPK(24L, 3L));
                // accounts.put("lewy_official", new EntityTweetPK(24L, 3L));
                // accounts.put("vardy7", new EntityTweetPK(24L, 3L));
                // accounts.put("VirgilvDijk", new EntityTweetPK(24L, 3L));
                // accounts.put("IngsDanny", new EntityTweetPK(24L, 3L));
                // accounts.put("ErlingHaaland", new EntityTweetPK(24L, 3L));
                // accounts.put("MoSalah", new EntityTweetPK(24L, 3L));

                // accounts.put("Nashgrier", new EntityTweetPK(25L, 3L));
                // accounts.put("TTfue", new EntityTweetPK(25L, 3L));
                // accounts.put("TheRealRyanHiga", new EntityTweetPK(25L, 3L));
                // accounts.put("zachking", new EntityTweetPK(25L, 3L));
                // accounts.put("Ninja", new EntityTweetPK(25L, 3L));
                // accounts.put("markiplier", new EntityTweetPK(25L, 3L));
                // accounts.put("smosh", new EntityTweetPK(25L, 3L));
                // accounts.put("lizakoshy", new EntityTweetPK(25L, 3L));
                // accounts.put("DudePerfect", new EntityTweetPK(25L, 3L));
                // accounts.put("Jack_Septic_Eye", new EntityTweetPK(25L, 3L));

                for (Entry<String, EntityTweetPK> account : accounts.entrySet()) {
                        EntityTweet entity = _entities.findById(account.getValue()).get();

                        TwitterUser twitterUser = _twitterUsers
                                        .getUserByScreenName(account.getKey())
                                        .orElse(_twitterService.getInstance()
                                                        .createUser(account.getKey()));

                        _twitterService.getInstance().parseResultJson(twitterUser, entity);
                }

                return ResponseEntity.ok().build();
        }
}
