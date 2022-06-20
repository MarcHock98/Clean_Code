package com.cleancode.testen.tabellieren;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ParserService {

    public static List<String> parseCsvToTable(List<String> csvLines) {
        List<List<String>> csvLineList = convertCsvLinesToList(csvLines);
        List<List<String>> orderedColumns = orderColumns(csvLineList);
        List<Integer> maxColumnWidths = calculateMaximumWidthOfColumn(orderedColumns);
        List<List<String>> tableWithAddedSpaces = addMaxWidthToColumnElements(orderedColumns, maxColumnWidths);
        List<String> lines = parseMultipleLines(csvLines);
        lines.add(1, getSeparatorLine());

        return lines;
    }

    public static List<List<String>> addMaxWidthToColumnElements(List<List<String>> table, List<Integer> maxColumnWidths) {
        int i = 0;
        List<List<String>> listWithSpaces = new ArrayList<>(List.of());
        for(List<String> column: table) {
            listWithSpaces.add(addSpacesForEachElementOfList(column, maxColumnWidths.get(i)));
            i++;
        }
        return listWithSpaces;
    }

    public static List<String> addSpacesForEachElementOfList(List<String> list, int maxColumnWidths) {
        List<String> listWithSpaces = new ArrayList<>();
        for (String element : list) {
            int numberOfSpacesToBeAdded = maxColumnWidths - element.length();
            element = element + " ".repeat(Math.max(0, numberOfSpacesToBeAdded));
            listWithSpaces.add(element);
        }
        return listWithSpaces;
    }

    public static List<Integer> calculateMaximumWidthOfColumn(List<List<String>> csvLineList) {
        List<Integer> columnWidths = new ArrayList<>(List.of());
        csvLineList.forEach(csvLine -> csvLine.stream().max(Comparator.comparing(String::length)).ifPresent(longest -> columnWidths.add(longest.length())));
        return columnWidths;
    }

    public static List<List<String>> orderColumns(List<List<String>> splittedCsvLines) {
        List<List<String>> orderedColumns = new ArrayList<>(List.of());
        while (!splittedCsvLines.get(0).isEmpty()) {
            List<String> column = new ArrayList<>(List.of());
            for (List<String> row : splittedCsvLines) {
                String elementToBeAddedToList = row.stream().findFirst().orElse("");
                column.add(elementToBeAddedToList);
                row.remove(elementToBeAddedToList);
            }
            orderedColumns.add(column);
        }
        return orderedColumns;
    }

    private static List<List<String>> convertCsvLinesToList(List<String> csvLines) {
        List<List<String>> splittedCsvLines = new ArrayList<>(List.of());
        csvLines.forEach(line -> splittedCsvLines.add(Arrays.stream(line.split(";")).collect(Collectors.toList())));
        return splittedCsvLines;
    }

    public static String parseSingleLine(String rawCsvLine) {
        List<String> splittedCsvLine = Arrays.stream(rawCsvLine.split(";")).collect(Collectors.toList());
        StringBuilder parsedLine = new StringBuilder();

        for (String csvLine : splittedCsvLine) {
            parsedLine.append(csvLine).append(" | ");
        }

        log.info(parsedLine.toString());
        return parsedLine.toString();
    }

    private static List<String> parseMultipleLines(List<String> csvLines) {
        List<String> parsedLines = new ArrayList<>(Collections.emptyList());

        for (String line : csvLines) {
            parsedLines.add(parseSingleLine(line));
        }
        return parsedLines;
    }

    public static List<String> parseColumnTableToCsv(List<List<String>> table) {
        List<String> lines = new ArrayList<>();
        List<List<String>> table_ = table;


        while (!table_.get(0).isEmpty()) {
            StringBuilder row = new StringBuilder();
            for (List<String> column : table_) {
                String elementToBeAddedToList = column.stream().findFirst().orElse("");
                row.append(elementToBeAddedToList).append(";");
                column.remove(elementToBeAddedToList);
            }
            lines.add(row.toString());
        }
        return lines;
    }

    private static String getSeparatorLine() {
        return "--------------+------------------+---------------+-------+";
    }
}
