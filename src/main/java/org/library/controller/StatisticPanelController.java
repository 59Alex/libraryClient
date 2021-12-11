package org.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import org.library.ServerSender.StatisticSender;
import org.library.alerts.NewWindow;


import java.net.URL;
import java.util.ResourceBundle;

public class StatisticPanelController implements Initializable {

    @FXML
    private Button backBtn;
    @FXML
    private PieChart statistic;

    private StatisticSender statisticSender;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.statisticSender = new StatisticSender();
        Long bookQ = this.statisticSender.getAllBookQ();
        Long journalQ = this.statisticSender.getAllJournalQ();
        Long newspaperQ = this.statisticSender.getAllNewspaperQ();
        if(bookQ != null && journalQ != null && newspaperQ != null) {
            initChart(bookQ, journalQ, newspaperQ);
        }


        backBtn.setOnAction(event -> {
            this.statisticSender.esc();
            NewWindow.openWindow("/adminPanel.fxml");
        });
    }

    private void initChart(Long bookQ, Long journalQ, Long newspaperQ) {
        PieChart.Data slice1 = new PieChart.Data("Книги", bookQ);
        PieChart.Data slice2 = new PieChart.Data("Журналы", journalQ);
        PieChart.Data slice3 = new PieChart.Data("Газеты", newspaperQ);

        statistic.getData().add(slice1);
        statistic.getData().add(slice2);
        statistic.getData().add(slice3);
    }
}
