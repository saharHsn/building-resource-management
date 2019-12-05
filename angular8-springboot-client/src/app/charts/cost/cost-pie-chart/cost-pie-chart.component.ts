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
  buildingId: string;
  highcharts = Highcharts;
  loading = true;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.costPieData(this.buildingId)
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
                  enabled: false
                },

                showInLegend: true
              }
            },
            series: [{
              type: 'pie',

              data: [
                {
                  name: 'Contracted\n' +
                    'Power',
                  y: data.content.contractedPower,
                  color: '#0066cc'
                },
                {
                  name: 'Off-hours',
                  y: data.content.offHours,
                  color: '#248f24'
                },
                {
                  name: 'Free-hours',
                  y: data.content.freeHours,
                  color: '#ffff00'
                },
                {
                  name: 'Peak-hours',
                  y: data.content.peakHours,
                  color: '#ff0000'
                },
                {
                  name: 'Normal-hours',
                  y: data.content.normalHours,
                  color: '#ff944d'
                },
                {
                  name: 'Power in Peak Hours',
                  y: data.content.powerInPeakHours,
                  color: '#ff6666'
                },
                {
                  name: 'Reactive Power',
                  y: data.content.reactivePower,
                  color: '#ff00ff'
                },
              ]
            }]
          };
        },
        () => {
        });
  }
}
