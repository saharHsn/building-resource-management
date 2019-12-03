package tech.builtrix.util;

import com.amazonaws.services.textract.model.Block;
import com.amazonaws.services.textract.model.Relationship;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By sahar at 11/29/19
 */
public class TextractUtil {
    public static List<MyTable> extractTables(Map<String, Block> blocksMap, List<Block> tableBlocks) {
        //String csv = "";
        List<MyTable> tables = new ArrayList<>();
        for (Block tableBlock : tableBlocks) {
            //getRowsColumnsMap(blocksMap, tableBlock);
            //csv += generateTableCsv(tableBlock, blocksMap, tableBlocks.indexOf(tableBlock) + 1);
            //csv += "\n\n";
            tables.add(generateTable(tableBlock, blocksMap));
        }
        return tables;
    }

    private static String generateTableCsv(Block tableBlock, Map<String, Block> blocksMap, int index) {
        Map<Integer, List<String>> rowsColumnsMap = getRowsColumnsMap(blocksMap, tableBlock);
        String tableId = "Table_" + index;
        String csv = tableId + "\n";
        for (Integer rowIndex : rowsColumnsMap.keySet()) {
            for (String rowText : rowsColumnsMap.get(rowIndex)) {
                csv += rowText + ",";
            }
        }
        csv += "\n\n\n";
        return csv;
    }

    private static MyTable generateTable(Block tableBlock, Map<String, Block> blocksMap) {
        MyTable table = new MyTable();
        Map<Integer, List<String>> rowsColumnsMap = getRowsColumnsMap(blocksMap, tableBlock);
        List<String> rowHeaders = rowsColumnsMap.get(1);
        Map<String, List<String>> column_value = new HashMap<>();
        for (Integer rowIndex : rowsColumnsMap.keySet()) {
            List<String> columnValues = rowsColumnsMap.get(rowIndex);
            column_value.put(columnValues.get(0), columnValues.subList(1, columnValues.size()));
        }
        table.setColumn_value(column_value);
        table.setRowHeaders(rowHeaders);
        return table;
    }

    private static Map<Integer, List<String>> getRowsColumnsMap(Map<String, Block> blocksMap, Block tableBlock) {
        Map<Integer, List<String>> matrix = new HashMap<>();
        for (Relationship relationship : tableBlock.getRelationships()) {
            if (relationship.getType().equals("CHILD")) {
                for (String id : relationship.getIds()) {
                    Block cell = blocksMap.get(id);
                    if (cell.getBlockType().equals("CELL")) {
                        String text = getText(cell, blocksMap);
                        if (matrix.get(cell.getRowIndex()) == null) {
                            matrix.put(cell.getRowIndex(), new ArrayList<>());
                        } else {
                            List<String> colTexts = matrix.get(cell.getRowIndex());
                            if (CollectionUtils.isEmpty(colTexts)) {
                                matrix.put(cell.getRowIndex(), new ArrayList<>());
                            }
                        }
                        matrix.get(cell.getRowIndex()).add(text);
                    }
                }
            }
        }
        return matrix;
    }

    private static String getText(Block cell, Map<String, Block> blocksMap) {
        StringBuilder text = new StringBuilder();
        if (!CollectionUtils.isEmpty(cell.getRelationships())) {
            for (Relationship relationship : cell.getRelationships()) {
                if (relationship.getType().equals("CHILD")) {
                    for (String id : relationship.getIds()) {
                        Block word = blocksMap.get(id);
                        if (word.getBlockType().equals("WORD")) {
                            text.append(word.getText()).append(" ");
                        }
                        if (word.getBlockType().equals("SELECTION_ELEMENT")) {
                            if (word.getSelectionStatus().equals("SELECTED")) {
                                text.append("X ");
                            }
                        }
                    }
                }
            }
        }
        return text.toString();
    }

    public static Map<String, String> extractKeyValues(Map<String, Block> blocksMap, Map<String, Block> keysMap, Map<String, Block> valuesMap) {
        Map<String, String> kvs = get_kv_relationship(keysMap, valuesMap, blocksMap);
        System.out.println("\n\n== FOUND KEY : VALUE pairs ===\n");
        //return print_kvs(kvs);
        return kvs;
    }

    private static Map<String, String> get_kv_relationship(Map<String, Block> key_map,
                                                           Map<String, Block> value_map,
                                                           Map<String, Block> block_map) {
        Map<String, String> kvs = new HashMap<>();
        for (String s : key_map.keySet()) {
            Block key_block = key_map.get(s);
            Block value_block = find_value_block(key_block, value_map);
            String key = get_text(key_block, block_map);
            String val = get_text(value_block, block_map);
            kvs.put(key, val);
        }
        return kvs;
    }

    private static String get_text(Block value_block, Map<String, Block> block_map) {
        String text = "";
        if (!CollectionUtils.isEmpty(value_block.getRelationships())) {
            for (Relationship relationship : value_block.getRelationships()) {
                for (String id : relationship.getIds()) {
                    Block word = block_map.get(id);
                    if (word.getBlockType().equals("WORD")) {
                        text += word.getText() + ' ';
                    }
                    if (word.getBlockType().equals("SELECTION_ELEMENT")) {
                        if (word.getSelectionStatus().equals("SELECTED")) {
                            text += "X ";
                        }
                    }
                }
            }
        }
        return text;
    }

    private static Block find_value_block(Block key_block, Map<String, Block> value_map) {
        Block value_block = null;
        if (!CollectionUtils.isEmpty(key_block.getRelationships())) {
            for (Relationship relationship : key_block.getRelationships()) {
                if (relationship.getType().equals("VALUE")) {
                    for (String id : relationship.getIds()) {
                        value_block = value_map.get(id);
                    }
                }
            }
        }
        return value_block;
    }


    private static String search_value(Map<String, String> kvs, String search_key) {
        for (String key : kvs.keySet()) {
            if (kvs.get(key).equalsIgnoreCase(search_key)) {
                return kvs.get(key);
            }
        }
        return null;
    }


    private static String print_kvs(Map<String, String> kvs) {
        String x = "";
        for (String s : kvs.keySet()) {
            x += s + ":" + kvs.get(s) + "\n";
        }
        return x;
    }

    public static TExtractDto extractData(List<Block> blocks) {
        Map<String, Block> blocksMap = new HashMap<>();
        Map<String, Block> keysMap = new HashMap<>();
        Map<String, Block> valuesMap = new HashMap<>();
        List<Block> tableBlocks = new ArrayList<>();

        for (Block block : blocks) {
            blocksMap.put(block.getId(), block);
            if (block.getBlockType().equals("TABLE")) {
                tableBlocks.add(block);
            }
            if (block.getBlockType().equals("KEY_VALUE_SET")) {
                if (block.getEntityTypes().contains("KEY")) {
                    keysMap.put(block.getId(), block);
                } else {
                    valuesMap.put(block.getId(), block);
                }
            }
        }

        Map<String, String> keyValues = TextractUtil.extractKeyValues(blocksMap, keysMap, valuesMap);
        List<MyTable> tableResult = TextractUtil.extractTables(blocksMap, tableBlocks);

        return new TExtractDto(tableResult, keyValues);
    }

}
