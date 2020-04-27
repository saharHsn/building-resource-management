import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {Router} from '@angular/router';

import {ChartService} from '../../chartService';
import {Building} from '../../../building/model/building';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-predictions',
  templateUrl: './predictions.component.html',
  styleUrls: ['./predictions.component.css']
})
export class PredictionsComponent implements OnInit {
  loading = true;
  Highcharts: any;
  chartOptions: any;

  xValues: string[];
  costYValues: number[];
  savingYValues: number[];
  building: Building;
  highcharts = Highcharts;

  constructor(private chartService: ChartService,
              private router: Router) {

  }

  ngOnInit() {
    this.chartService.predict()
      .pipe(first())
      .subscribe(
        data => {
          // this.chartOptions.series = [{
          //     data: data
          // }];
          this.Highcharts = Highcharts;
          this.chartOptions = {
            chart: {
              type: 'spline',
              /*  backgroundColor: null,
               width: 330,
               , */
              grid: false,
              backgroundColor: null
            },
            title: {
              text: 'Predicted cost'
            },
            xAxis: {
              // categories: ['Oct-2019', 'Nov-2019', 'Dec-2019'],
              categories: data.content.xvalues,
              labels: {
                style: {
                  fontSize: '15px'
                }
              }
            },
            credits: {
              enabled: false
            }
            ,
            yAxis: {
              title: {
                text: '€'
              },
              labels: {
                style: {
                  fontSize: '15px'
                }
              },
              /*  categories: [0, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500],
               tickInterval: 500 */
            },
            tooltip: {
              valueSuffix: ' €'
            },
            series: [/* {
              name: 'Saving',
              // data: [321, 420, 360]
              data: data.content.savingYValues,
              dashStyle: 'shortdot',
              color: ''

            }, */
              {
                name: 'Cost',
                // data: [6135.5, 7130.4, 6234.3]
                data: data.content.costYValues,
                dashStyle: 'shortdot',
                color: '#4C5E81'
              }
            ]
          };
          this.loading = false;
        },
        () => {
        });
  }
}
