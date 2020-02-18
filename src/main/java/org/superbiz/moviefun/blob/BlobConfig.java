package org.superbiz.moviefun.blob;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;

@Configuration
public class BlobConfig {
	
	@Bean
    ServiceCredentials serviceCredentials(@Value("${vcap.services}") String vcapServices) {
        return new ServiceCredentials(vcapServices);
    }

    @Bean
    public BlobStore blobStore(
        ServiceCredentials serviceCredentials,
        @Value("${vcap.services.photo-storage.credentials.endpoint:#{null}}") String endpoint
    ) {
        String photoStorageAccessKeyId = serviceCredentials.getCredential("photo-storage", "user-provided", "access_key_id");
        String photoStorageSecretKey = serviceCredentials.getCredential("photo-storage", "user-provided", "secret_access_key");
        String photoStorageBucket = serviceCredentials.getCredential("photo-storage", "user-provided", "bucket");
        
        StaticCredentialsProvider scp = StaticCredentialsProvider.create(
        		AwsBasicCredentials.create(photoStorageAccessKeyId, photoStorageSecretKey));
        
        S3Client s3Client = S3Client.builder()
        		 .region(Region.US_EAST_2)
        		.credentialsProvider(scp)
        		.build();        

        return new S3Store(s3Client, photoStorageBucket);
    }

}
