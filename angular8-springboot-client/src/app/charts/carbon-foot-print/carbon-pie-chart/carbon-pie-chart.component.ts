import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-carbon-pie-chart',
  templateUrl: './carbon-pie-chart.component.html',
  styleUrls: ['./carbon-pie-chart.component.css']
})
export class CarbonPieChartComponent implements OnInit {
  buildingId: string;
  highcharts = Highcharts;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.carbonPieData(this.buildingId)
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
              text: 'Average Contribution of Tariffs in Carbon Footprint'
            },
            tooltip: {
              pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
              pie: {
                allowPointSelect: true,
                cursor: 'pointer',

                dataLabels: {
                  enabled: true,
                  format: '<br>{point.percentage:.1f} %',
                  distance: -50,
                  filter: {
                    property: 'percentage',
                    operator: '>',
                    value: 4
                  }
                },

                showInLegend: true
              }
            },

            series: [{
              type: 'pie',
              data: [
                {
                  name: 'CO2 Off-hours',
                  // y: 11.4,
                  y: data.content.co2Off,
                  color: '#0099ff'
                },
                {
                  name: 'CO2 Free-hours',
                  // y: 19.4,
                  y: data.content.co2Free,
                  color: '#ffff00'
                },
                {
                  name: 'CO2 Peak-hours',
                  // y: 18.1,
                  y: data.content.co2Peak,
                  color: '#ff0000'
                },
                {
                  name: 'CO2 Normal-hours',
                  // y: 51,
                  y: data.content.co2Normal,
                  color: '#ff944d'
                }
              ]
            }]
          };
        },
        () => {
        });
  }
}
