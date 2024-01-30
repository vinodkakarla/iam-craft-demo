package com.intuit.interview.demo.resource;

//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
        import software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider;
        import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

        import java.security.Principal;

@RestController
public class DocStoreController {

//    @Value("${aws.access-key}")
//    private String awsAccessKey;
//
//    @Value("${aws.secret-key}")
//    private String awsSecretKey;
//
//    @Value("${aws.s3.bucket-name}")
//    private String s3BucketName;

    @GetMapping("/doc-store")
    public String getS3Objects(Principal principal) {
        // Obtain S3 client using AWS SDK for Java v2
//        S3Client s3Client = S3Client.builder()
//                .region(Region.US_EAST_1)  // Change to your desired region
//                .credentialsProvider(() -> AwsBasicCredentials.create(awsAccessKey, awsSecretKey))
//                .endpointOverride(URI.create("https://s3.amazonaws.com"))  // Change to your S3 endpoint
//                .build();

        String bucketName = "s3-develop";
        Region region = Region.AP_NORTHEAST_1;

        // Create an S3 client with anonymous credentials for public buckets
        S3Client s3Client = S3Client.builder()
                .region(region)
                .credentialsProvider(AnonymousCredentialsProvider.create())
                .build();

        // Your S3 logic here
        // Example: List S3 objects
        s3Client.listObjectsV2(builder -> builder.bucket(bucketName)).contents().forEach(System.out::println);

        return "S3 Objects Listed!";
    }


//    @Configuration
//    public static class OktaWebClientConfig {
//
//        @Bean
//        WebClient webClient(ClientRegistrationRepository clientRegistrations,
//                            OAuth2AuthorizedClientRepository authorizedClients) {
//            ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
//                    clientRegistrations, authorizedClients);
//            oauth2.setDefaultOAuth2AuthorizedClient(true);
//            oauth2.setDefaultClientRegistrationId("okta");
//            return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
//        }
//    }
}
