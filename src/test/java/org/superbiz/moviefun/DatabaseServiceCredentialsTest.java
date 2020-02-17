package org.superbiz.moviefun;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class DatabaseServiceCredentialsTest {

    @Test
    public void testDataSource() throws Exception {
        //language=JSON
        String vcapServices = "{\n" +
                "  \"p-mysql\": [\n" +
                "    {\n" +
                "      \"credentials\": {\n" +
                "        \"hostname\": \"192.168.16.98\",\n" +
                "        \"jdbcUrl\": \"jdbc:mysql://192.168.16.98:3306/cf_03c57552_aa73_479e_91b5_6ff5c64ee365?user=WzIT1E8B4B7AtO6M&password=GXNQn7qt1wJtxbV5\",\n" +
                "        \"name\": \"cf_03c57552_aa73_479e_91b5_6ff5c64ee365\",\n" +
                "        \"password\": \"GXNQn7qt1wJtxbV5\",\n" +
                "        \"port\": 3306,\n" +
                "        \"uri\": \"mysql://WzIT1E8B4B7AtO6M:GXNQn7qt1wJtxbV5@192.168.16.98:3306/cf_03c57552_aa73_479e_91b5_6ff5c64ee365?reconnect=true\",\n" +
                "        \"username\": \"WzIT1E8B4B7AtO6M\"\n" +
                "      },\n" +
                "      \"label\": \"p-mysql\",\n" +
                "      \"name\": \"albums-mysql\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"credentials\": {\n" +
                "        \"hostname\": \"192.168.16.98\",\n" +
                "        \"jdbcUrl\": \"jdbc:mysql://wrong-db\",\n" +
                "        \"name\": \"wrong-db-name\",\n" +
                "        \"password\": \"wrong-password\",\n" +
                "        \"port\": 3306,\n" +
                "        \"uri\": \"mysql://uri\",\n" +
                "        \"username\": \"foo\"\n" +
                "      },\n" +
                "      \"label\": \"p-mysql\",\n" +
                "      \"name\": \"movies-mysql\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        DatabaseServiceCredentials serviceCredentials = new DatabaseServiceCredentials(vcapServices);

        String jdbcUrl = serviceCredentials.jdbcUrl("albums-mysql");

        assertThat(jdbcUrl, equalTo("jdbc:mysql://192.168.16.98:3306/cf_03c57552_aa73_479e_91b5_6ff5c64ee365?user=WzIT1E8B4B7AtO6M&password=GXNQn7qt1wJtxbV5"));

    }
}
