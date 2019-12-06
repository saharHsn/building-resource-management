import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-consumption-weather',
  templateUrl: './consumption-weather.component.html',
  styleUrls: ['./consumption-weather.component.css']
})
export class ConsumptionWeatherComponent implements OnInit {
  buildingId: string;
  highcharts = Highcharts;
  chartOptions: any;

  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.normalizedConsumptionWeatherData(this.buildingId)
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              type: 'column',
              backgroundColor: null,
              width: 800,
              height: 500,
              grid: true,
              gridLineColor: '#0066cc'
            },
            title: {
              text: ''
            },
            xAxis: {
              /*categories: ['Jan-2018',
                'Feb-2018',
                'Mar-2018',
                'Apr-2018',
                'May-2018',
                'Jun-2018',
                'Jul-2018',
                'Aug-2018',
                'Sep-2018',
                'Oct-2018',
                'Nov-2018',
                'Dec-2018'],*/
              categories: data.content.xValues,
              labels: {
                style: {
                  fontSize: '15px'
                }
              }
            },
            yAxis: {
              gridLineWidth: 0,

              title: {
                text: 'kWh/m2/HCDD'
              },
              labels: {
                style: {
                  fontSize: '15px'
                }
              },
            },
            legend: {
              reversed: false
            },
            tooltip: {
              valueSuffix: 'kWh/m2/HCDD'
            },
            plotOptions: {
              column: {
                stacking: 'normal'
              }
            },
            series: [
              {
                name: 'Standard A',
                // data: [0, 0, 0, 0.042, 0, 0, 0, 0, 0, 0, 0, 0],
                data: data.content.standardAValues,
                color: '#1fc105'
              },
              {
                name: 'Standard B',
                // data: [0, 0, 0, 0, 0.07, 0.092, 0, 0.08, 0.067, 0, 0, 0],
                data: data.content.standardBValues,
                color: '#ffff33'
              },
              {
                name: 'Standard C',
                // data: [0.04, 0.04, 0.045, 0, 0, 0, 0.14, 0, 0, 0.092, 0.066, 0.05],
                data: data.content.standardCValues,
                color: '#fc8a09'
              },
              {
                name: 'Standard D',
                // data: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                data: data.content.standardDValues,
                color: '#ff4817'
              },
            ]
          };
        },
        () => {
        });
  }

}
