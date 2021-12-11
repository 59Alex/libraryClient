package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;

public class StatisticSender {

    private Client client;

    private static final String statisticKey = "statistic";
    private static final String allQuantityBook = "allQBook";
    private static final String allQuantityJournal = "allQJounal";
    private static final String allQuantityNewspaper = "allQNewspaper";
    private static final String esc = "esc";

    public StatisticSender() {
        this.client = Client.getClient();
    }

    public Long getAllBookQ() {
        ToServer.send(statisticKey, client.getOut());
        ToServer.send(allQuantityBook, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public Long getAllJournalQ() {
        ToServer.send(statisticKey, client.getOut());
        ToServer.send(allQuantityJournal, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public Long getAllNewspaperQ() {
        ToServer.send(statisticKey, client.getOut());
        ToServer.send(allQuantityNewspaper, client.getOut());
        return ToServer.accept(client.getInput());
    }
    public void esc() {
        ToServer.send(statisticKey, client.getOut());
        ToServer.send(esc, client.getOut());
    }
}
