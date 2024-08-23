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

import lombok.extern.slf4j.Slf4j;
import nl.knaw.dans.lib.util.AbstractCommandLineApp;
import nl.knaw.dans.lib.util.PicocliVersionProvider;
import nl.knaw.dans.validatecli.config.DdValidateDansBagCliConfig;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "dans-bag-validate",
         mixinStandardHelpOptions = true,
         versionProvider = PicocliVersionProvider.class,
         description = "Command-line client for validating DANS bags")
@Slf4j
public class DdValidateDansBagCli extends AbstractCommandLineApp<DdValidateDansBagCliConfig> {
    public static void main(String[] args) throws Exception {
        new DdValidateDansBagCli().run(args);
    }

    public String getName() {
        return "Command-line client for validating DANS bags";
    }

    @Override
    public void configureCommandLine(CommandLine commandLine, DdValidateDansBagCliConfig config) {
        // TODO: set up the API client, if applicable
        log.debug("Configuring command line");
        // TODO: add options and subcommands
    }
}
