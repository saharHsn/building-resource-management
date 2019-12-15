import {Component, OnInit} from '@angular/core';
import * as Highcharts from 'highcharts';
import {first} from 'rxjs/operators';
import {ChartService} from '../../chartService';
import {Router} from '@angular/router';

@Component({
  selector: 'app-normalized-per-capita',
  templateUrl: './normalized-per-capita.component.html',
  styleUrls: ['./normalized-per-capita.component.css']
})
export class NormalizedPerCapitaComponent implements OnInit {
  highcharts = Highcharts;
  chartOptions: any;


  constructor(private chartService: ChartService,
              private router: Router) {
  }

  ngOnInit() {
    this.chartService.normalizedPerCapitaData()
      .pipe(first())
      .subscribe(
        data => {
          this.highcharts = Highcharts;


          const content = data.content;
          this.chartOptions = {
            chart: {
              type: 'spline',
              backgroundColor: null,
              grid: true,
              width: 800,
              height: 500
            },
            title: {
              text: ''
            },
            xAxis: {
              categories: content.xvalues,
              labels: {
                style: {
                  fontSize: '15px'
                }
              }
            },
            yAxis: {
              title: {
                text: 'kWh/cap'
              },
              labels: {
                style: {
                  fontSize: '15px'
                }
              },
            },
            tooltip: {
              valueSuffix: 'kWh/cap'
            },
            series: [
              {
                name: 'Baseline',
                // data: [98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13, 98.13],
                data: content.baseLine,
                color: '#0033cc'
              },
              {
                name: 'Total',
                // data: [116.351, 122.56, 107.364, 110.476, 88.964, 87.093, 95.84, 104.204, 100.311, 102.862, 105.511, 122.884],
                data: content.total,
                color: '#0b0003'
              }
            ]
          };
        },
        () => {
        });
  }

}
