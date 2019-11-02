import {Component, OnDestroy, OnInit} from '@angular/core';
import 'highcharts/modules/exporting';
import * as more from 'highcharts-more/more';
import * as HighchartsSolidGauge from 'highcharts/modules/solid-gauge';
import * as Highcharts from 'highcharts/highcharts';
import * as HighchartsMore from 'highcharts/highcharts-more';

// Now init modules:
// @ts-ignore
HighchartsMore(Highcharts);
// @ts-ignore
HighchartsSolidGauge(Highcharts);
more(Highcharts);

@Component({
  selector: 'app-be-score',
  templateUrl: './be-score.component.html',
  styleUrls: ['./be-score.component.css'],
})

export class BeScoreComponent implements OnInit, OnDestroy {
  // private updataDataEndRef: Subscription = null;
  public messageCount: number;
  public chart: any;

  // @Input() usageService: UsageService;

  constructor() {
    this.messageCount = 0;
  }

  ngOnInit(): void {
    this.initChart(this.buildGauge());
  }

  ngOnDestroy() {
    // this.updataDataEndRef.unsubscribe();
  }

  // noinspection JSMethodCanBeStatic
  private buildGauge(): any {
    return {
      chart: {
        type: 'gauge',
        backgroundColor: null,
        plotBackgroundColor: null,
        plotBackgroundImage: null,
        plotBorderWidth: 0,
        plotShadow: false
      },
      title: 'Speedometer',
      pane: {
        center: ['47%', '51%'],
        size: '70%',
        startAngle: -150,
        endAngle: 150,
        background: [{
          backgroundColor: {
            linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
            stops: [
              [0, '#FFF'],
              [1, '#333']
            ]
          },
          borderWidth: 0,
          outerRadius: '109%'
        }, {
          backgroundColor: {
            linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
            stops: [
              [0, '#333'],
              [1, '#FFF']
            ]
          },
          borderWidth: 1,
          outerRadius: '107%'
        }, {
          // default background
        }, {
          backgroundColor: '#DDD',
          borderWidth: 0,
          outerRadius: '105%',
          innerRadius: '103%'
        }]
      },
      tooltip: {
        enabled: false
      },
      yAxis: {
        min: 0,
        max: 55,

        minorTickInterval: 'auto',
        minorTickWidth: 1,
        minorTickLength: 10,
        minorTickPosition: 'inside',
        minorTickColor: '#666',

        tickPixelInterval: 30,
        tickWidth: 2,
        tickPosition: 'inside',
        tickLength: 10,
        tickColor: '#666',
        labels: {
          step: 2,
          rotation: 'auto'
        },
        title: {
          text: 'BE Score',
          style: {
            fontSize: '15px',
            bold: true
          }
        },
        plotBands: [{
          from: 0,
          to: 27,
          color: '#55BF3B' // green
        }, {
          from: 27,
          to: 50,
          color: '#DDDF0D'  // yellow
        }, {
          from: 50,
          to: 55,
          color: '#DF5353' // red
        }]
        /*stops: [
          [0.1, '#55BF3B'],
          [0.5, '#DDDF0D'],
          [0.9, '#DF5353']
        ],
        lineWidth: 0,
        minorTickInterval: null,
        tickAmount: 2,
        title: {
          y: -70
        },
        labels: {
          y: 16
        }*/
      },
      series: [{
        name: 'Speed',
        data: [80],
        tooltip: {
          valueSuffix: ' km/h'
        }
      }]
      /*plotOptions: {
        solidgauge: {
          dataLabels: {
            y: 5,
            borderWidth: 0,
            useHTML: true
          }
        }
      }*/
    };
  }

  private initChart(gaugeOptions: any) {
    this.chart = Highcharts.chart('gauge-container',
      Highcharts.merge(gaugeOptions, {},
        // Add some life
        // tslint:disable-next-line:only-arrow-functions
        function (chart) {
          if (!chart.renderer.forExport) {
            // tslint:disable-next-line:only-arrow-functions
            setInterval(function () {
              // tslint:disable-next-line:one-variable-per-declaration prefer-const
              let point = chart.series[0].points[0],
                newVal,
                // tslint:disable-next-line:prefer-const
                inc = Math.round((Math.random() - 0.5) * 20);

              newVal = point.y + inc;
              if (newVal < 0 || newVal > 200) {
                newVal = point.y - inc;
              }

              point.update(newVal);

            }, 3000);
          }
        }
        /**/
      ))
    ;
  }
}
