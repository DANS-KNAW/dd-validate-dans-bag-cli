/*
 * Copyright (C) 2024 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.knaw.dans.validatecli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.util.AbstractCommandLineApp;
import nl.knaw.dans.lib.util.ClientProxyBuilder;
import nl.knaw.dans.lib.util.PicocliVersionProvider;
import nl.knaw.dans.validatecli.api.ValidateCommandDto;
import nl.knaw.dans.validatecli.api.ValidateCommandDto.PackageTypeEnum;
import nl.knaw.dans.validatecli.api.ValidateOkDto;
import nl.knaw.dans.validatecli.client.ApiClient;
import nl.knaw.dans.validatecli.client.ApiException;
import nl.knaw.dans.validatecli.client.DefaultApi;
import nl.knaw.dans.validatecli.config.DdValidateDansBagCliConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;

@Command(name = "dans-bag-validate",
         mixinStandardHelpOptions = true,
         versionProvider = PicocliVersionProvider.class,
         description = "Command-line client for validating DANS bags")
@Slf4j
public class DdValidateDansBagCli extends AbstractCommandLineApp<DdValidateDansBagCliConfig> {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private DefaultApi api;
    private PackageTypeEnum defaultPackageType;

    public static void main(String[] args) throws Exception {
        new DdValidateDansBagCli().run(args);
    }

    public String getName() {
        return "Command-line client for validating DANS bags";
    }

    @Parameters(index = "0",
                description = "The path to the bag to validate")
    private File bagPath;

    @Option(names = { "-p", "--package-type  " },
            description = "The type of package to validate (DEPOSIT or MIGRATION)")
    private PackageTypeEnum packageType;

    @Override
    public void configureCommandLine(CommandLine commandLine, DdValidateDansBagCliConfig config) {
        api = new ClientProxyBuilder<ApiClient, DefaultApi>()
            .apiClient(new ApiClient())
            .basePath(config.getValidateDansBagService().getUrl())
            .httpClient(config.getValidateDansBagService().getHttpClient())
            .defaultApiCtor(DefaultApi::new)
            .build();
        log.debug("Configuring command line");
        defaultPackageType = config.getValidateDansBagService().getDefaultPackageType();
    }

    @Override
    public Integer call() {
        try {
            ValidateOkDto result = null;
            if (bagPath.isFile()) {
                result = api.validateZipPost(bagPath);
            }
            else {
                var command = new ValidateCommandDto()
                    .bagLocation(bagPath.toPath().toString())
                    .packageType(packageType != null ? packageType : defaultPackageType);
                result = api.validateLocalDirPost(command);
            }
            System.out.println(MAPPER.writeValueAsString(result));
            return 0;
        }
        catch (ApiException | JsonProcessingException e) {
            System.err.println("Error while validating bag: " + e.getMessage());
            e.printStackTrace();
            log.error("Error while validating bag", e);
            return 1;
        }
    }
}
