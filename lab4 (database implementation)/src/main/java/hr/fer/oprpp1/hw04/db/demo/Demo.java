package hr.fer.oprpp1.hw04.db.demo;

import hr.fer.oprpp1.hw04.db.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Demo {
    public static void main(String[] args) {

        StudentDatabase sdb;
        try {
            sdb = new StudentDatabase(QueryIOUtils.loadDatabase("src/main/resources/database.txt"));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load student database.", e);
        }

        String query;

        while (!((query = QueryIOUtils.prompt()).equals("exit"))) {

            QueryParser parser = new QueryParser(query);

            List<StudentRecord> records = new ArrayList<>();
            if (parser.isDirectQuery()) {
                StudentRecord r = sdb.forJMBAG(parser.getQueriedJMBAG());
                if (r != null) {
                    records.add(r);
                }

                System.out.println("Using index for record retrieval.");
            } else {
                try {
                    records.addAll(sdb.filter(new QueryFilter(parser.getQuery())));
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + '\n');
                    continue;
                }
            }

            try {
                Objects.requireNonNull(RecordFormatter.format(records)).forEach(System.out::println);
            } catch (NullPointerException ignored) {

            } finally {
                System.out.println("Records selected: " + records.size());
                System.out.println();
            }
        }
    }
}
