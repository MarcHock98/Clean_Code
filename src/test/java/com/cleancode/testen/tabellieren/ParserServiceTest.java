package com.cleancode.testen.tabellieren;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserServiceTest {

    @Test
    void parseCsvToTable_TablesAreEquals() {
        // Arrange
        List<String> csvLines = List.of(
                "Name;Strasse;Ort;Alter",
                "Peter Pan;Am Hang 5;12345 Einsam;42",
                "Maria Schmitz;Kölner Straße 45;50123 Köln;43",
                "Paul Meier;Münchener Weg 1;87654 München;65"
        );

        List<String> expectedTable = List.of(
                "Name          | Strasse          | Ort           | Alter |",
                "--------------+------------------+---------------+-------+",
                "Peter Pan     | Am Hang 5        | 12345 Einsam  | 42    |",
                "Maria Schmitz | Kölner Straße 45 | 50123 Köln    | 43    |",
                "Paul Meier    | Münchener Weg 1  | 87654 München | 65    |"
        );

        // Act
        List<String> actual = ParserService.parseCsvToTable(csvLines);

        // Assert
        assertEquals(expectedTable, actual);
    }

    @Test
    void parseSingleLine_parseCsvLineToTableLine_lineIsParsed() {
        // Arrange
        String csvLine = "Name;Strasse;Ort;Alter";
        String expected = "Name | Strasse | Ort | Alter | ";

        // Act
        String actual = ParserService.parseSingleLine(csvLine);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void addSpacesForEachElementOfList_spacesWillBeAdded() {
        // Arrange
        List<String> stringsWithOutSpaces = List.of("Test_", "Test__", "Test___");

        // Act
        List<String> actual = ParserService.addSpacesForEachElementOfList(stringsWithOutSpaces, 7);

        // Assert
        assertEquals(7, actual.get(0).length());
        assertEquals(7, actual.get(1).length());
        assertEquals(7, actual.get(2).length());
    }

    @Test
    void calculateMaximumWidthOfColumn_widthWillBeCalculated() {
        // Arrange
        List<String> maxLengthIsFive = List.of("Test5", "ab", "d");
        List<String> maxLengthIsSix = List.of("ab", "TestL6", "d");
        List<String> maxLengthIsSeven = List.of("ab", "x", "TestL_7");

        List<List<String>> listToBeCalculatedMaxLengthOfColumns = List.of(maxLengthIsFive, maxLengthIsSix, maxLengthIsSeven);

        // Act
        List<Integer> actual = ParserService.calculateMaximumWidthOfColumn(listToBeCalculatedMaxLengthOfColumns);

        // Assert
        assertEquals(5, actual.get(0));
        assertEquals(6, actual.get(1));
        assertEquals(7, actual.get(2));
    }


    @Test
    void addMaxWidthToColumnElements_maxWidthWillBeAdded() {
        // Arrange
        List<String> maxLengthIsFive = List.of("Test5", "a", "b");
        List<String> maxLengthIsSix = List.of("a", "TestL6", "b");
        List<String> maxLengthIsSeven = List.of("a", "b", "TestL_7");

        List<Integer> maxColumnWidths = List.of(5, 6, 7);
        List<List<String>> listToBeCalculatedMaxLengthOfColumns = List.of(maxLengthIsFive, maxLengthIsSix, maxLengthIsSeven);

        // Act
        List<List<String>> actual = ParserService.addMaxWidthToColumnElements(listToBeCalculatedMaxLengthOfColumns, maxColumnWidths);

        // Assert
        assertEquals(5, actual.get(0).get(0).length());
        assertEquals(5, actual.get(0).get(1).length());
        assertEquals(5, actual.get(0).get(2).length());

        assertEquals(6, actual.get(1).get(0).length());
        assertEquals(6, actual.get(1).get(1).length());
        assertEquals(6, actual.get(1).get(2).length());

        assertEquals(7, actual.get(2).get(0).length());
        assertEquals(7, actual.get(2).get(1).length());
        assertEquals(7, actual.get(2).get(2).length());
    }


    @Test
    void orderColumns_orderListFromRowToColumn() {
        // Arrange
        List<String> row1 = List.of("Header1", "Header2", "Header3");
        List<String> row2 = List.of("col1", "col2", "col3");
        List<String> row3 = List.of("col1_2", "col2_2", "col3_2");

        List<List<String>> rowTable = List.of(row1, row2, row3);

        // Act
        List<List<String>> actual = ParserService.orderColumns(rowTable);

        // Assert
        List<String> column1 = List.of("Header1", "col1", "col1_2");
        List<String> column2 = List.of("Header2", "col2", "col2_2");
        List<String> column3 = List.of("Header3", "col3", "col3_2");

        List<List<String>> expected = List.of(column1, column2, column3);

        assertEquals(expected, actual);
    }

    @Test
    void parseColumnTableToRowTable_() {
        // Arrange
        List<String> column1 = List.of("Header1", "col1", "col1_2");
        List<String> column2 = List.of("Header2", "col2", "col2_2");
        List<String> column3 = List.of("Header3", "col3", "col3_2");

        List<List<String>> rowTable = List.of(column1, column2, column3);

        // Act
        List<String> actual = ParserService.parseColumnTableToCsv(rowTable);

        // Assert
        List<String> expected = List.of("Header1;Header2;Header3", "col1;col2;col3", "col1_2;col2_2;col3_2");
        assertEquals(expected, actual);
    }

}
