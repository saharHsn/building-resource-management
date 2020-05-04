import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-cost-pie-chart',
  templateUrl: './cost-pie-chart.component.html',
  styleUrls: ['./cost-pie-chart.component.css']
})
export class CostPieChartComponent implements OnInit {

  highcharts = Highcharts;
  loading = true;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.costPieData()
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              plotBorderWidth: null,
              plotShadow: false,
              backgroundColor: null
            },
            title: {
              text: 'Average Tariff Cost Structure'
            },
            tooltip: {
              pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
              pie: {
                allowPointSelect: true,
                cursor: 'pointer',

                dataLabels: {
                  enabled: true
                },

                showInLegend: true
              }
            },
            credits: {
              enabled: false
            }
            ,
            series: [{
              type: 'pie',

              data: [
                {
                  name: 'Contracted Power' +
                    'Power',
                  y: 6.1,    /* data.content.contractedPower, */
                  color: '#3A9AFC'
                },
                {
                  name: 'Off-hours',
                  y:  34.5, /* data.content.offHours, */
                  color: '#219653'
                },
                {
                  name: 'Free-hours',
                  y: 7.2, /* data.content.freeHours, */
                  color: '#55E27C'
                },
                {
                  name: 'Power in Peak Hours',
                  y: 7.62, /* data.content.peakHours, */
                  color: '#0B2161'
                },
                {
                  name: 'Normal-hours',
                  y: 34.5, /* data.content.normalHours, */
                  color: '#FFAB00'
                },
                {
                  name: 'Peak Hours',
                  y: 11.8, /*  data.content.powerInPeakHours, */
                  color: '#FF5630'
                },
                {
                  name: 'Reactive Power',
                  y: 4.02, /* data.content.reactivePower, */
                  color: '#6554C0'
                },
              ]
            }]
          };
        },
        () => {
        });
  }
}
