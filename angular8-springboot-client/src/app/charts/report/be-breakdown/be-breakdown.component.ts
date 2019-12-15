import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../chartService';
import * as Highcharts from 'highcharts';

@Component({
  selector: 'app-be-breakdown',
  templateUrl: './be-breakdown.component.html',
  styleUrls: ['./be-breakdown.component.css']
})
export class BeBreakdownComponent implements OnInit {
  Highcharts: any;
  chartOptions: any;

  xValues: string[];
  costYValues: number[];
  savingYValues: number[];

  highcharts = Highcharts;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService) {
  }

  ngOnInit(): void {
    this.initChart();
  }

  private initChart() {
    this.highcharts = Highcharts;
    this.chartOptions = {
      chart: {
        plotBorderWidth: null,
        plotShadow: false,
        backgroundColor: null,
        height: 300,
        width: 300,
        renderTo: 'BE Breakdown',
        type: 'pie'
      },
      title: {
        text: ''
      },

      credits: {
        enabled: false
      },
      plotOptions: {
        pie: {
          dataLabels: {
            enabled: false
          },
          size: 200,
          innerSize: '50%',
          center: ['50%', '40%']
        }
      },
      series: [{
        data: [{
          name: 'Yes',
          color: '#33cc33',
          y: 5
        }, {
          name: 'No',
          color: '#248f24',
          y: 95
        }]
      }]
    };
  }

}
