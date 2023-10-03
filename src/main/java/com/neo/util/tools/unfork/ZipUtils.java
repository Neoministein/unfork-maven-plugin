package com.neo.util.tools.unfork;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for ZipFiles
 */
public class ZipUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);

    /**
     * Unzips the zip archive
     *
     * @param archive zip archive
     * @param outputDir output directory
     * @param filter predicate if a file should at a path should be unzipped
     * @throws MojoExecutionException thrown if an IOException occurred
     */
    public void unzipArchive(File archive, File outputDir, Predicate<String> filter) throws MojoExecutionException {
        try(ZipFile zipfile = new ZipFile(archive)) {
            for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements();) {
                ZipEntry entry = e.nextElement();
                unzipEntry(zipfile, entry, outputDir, filter);
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Can not unizip " + archive, e);
        }
    }

    /**
     * Unzips on single file to the output directory
     *
     * @param filter
     *            The filter that decides if the file should be unzipped or not.
     * @param zipFile
     *            zipfile
     * @param entry
     *            entry
     * @param outputDir
     *            output directory name
     */
    private void unzipEntry(ZipFile zipFile, ZipEntry entry, File outputDir, Predicate<String> filter)
            throws IOException {

        if (entry.isDirectory()) {
            LOGGER.debug("The entry [{}] is a directory", entry.getName());
            return;
        }

        // ask filter if this file should be unzipped or not.
        if (!filter.test(entry.getName())) {
            LOGGER.debug("The entry [{}] will not be unzipped due to the provided filter", entry.getName());
            return;
        }

        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()) {
            LOGGER.debug("The entry [{}] requires new directories to be created [{}]", entry.getName(), outputDir.getParentFile().getAbsolutePath());
            Files.createDirectories(outputFile.getParentFile().toPath());
        }

        LOGGER.debug("Trying to creating entry [{}] at [{}]", entry.getName(), outputFile.getAbsolutePath());

        if (outputFile.exists()) {
            LOGGER.debug("The entry [{}] already exists at [{}]", entry.getName(), outputFile.getAbsolutePath());
            LOGGER.debug("Checking if files are identical...");
            try (BufferedInputStream existingInputStream = new BufferedInputStream(Files.newInputStream(outputFile.toPath()));
                 BufferedInputStream zippedInputStream = new BufferedInputStream(zipFile.getInputStream(entry))) {

                if (IOUtils.contentEquals(zippedInputStream, existingInputStream)) {
                    LOGGER.debug("The files are identical [{}]", outputFile.getAbsolutePath());
                    return;
                } else {
                    LOGGER.debug("The files are different [{}]", outputFile.getAbsolutePath());
                }

            }
        }

        try(BufferedInputStream inputStream = new BufferedInputStream(zipFile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            IOUtils.copy(inputStream, outputStream);
        }
    }
}