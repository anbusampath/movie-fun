package org.superbiz.moviefun.blob;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

public class S3Store implements BlobStore {

	private final S3Client s3;
	private final String bucketName;
	private final Tika tika = new Tika();

	public S3Store(S3Client s3, String bucketName) {
		this.s3 = s3;
		this.bucketName = bucketName;
	}

	@Override
	public void put(Blob blob) throws IOException {
		
		PutObjectRequest request = PutObjectRequest.builder()
													.bucket(bucketName)
													.key(blob.name)
													.build();
		
		s3.putObject(request, RequestBody.fromInputStream(blob.inputStream, blob.inputStream.available()));
	}

	@Override
    public Optional<Blob> get(String name) throws IOException {
    	
    	GetObjectRequest request = GetObjectRequest.builder()
    									.bucket(bucketName)
    									.key(name)
    									.build();
    	
    	ResponseBytes<GetObjectResponse> response = s3.getObject(request, 
    			ResponseTransformer.toBytes());

    	byte[] bytes = response.asByteArray();
    	
            return Optional.of(new Blob(
                name,
                new ByteArrayInputStream(bytes),
                tika.detect(bytes)));
    }

	@Override
	public void deleteAll() {

		ListObjectsRequest listObjects = ListObjectsRequest.builder().bucket(bucketName).build();
		ListObjectsResponse res = s3.listObjects(listObjects);
		List<S3Object> objects = res.contents();

		for (S3Object object : objects) {

			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(object.key())
					.build();

			s3.deleteObject(deleteObjectRequest);
		}

	}
}
