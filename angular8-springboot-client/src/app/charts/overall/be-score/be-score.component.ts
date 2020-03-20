import {Component, OnDestroy, OnInit} from '@angular/core';
import 'highcharts/modules/exporting';
import * as more from 'highcharts-more/more';
import * as HighchartsSolidGauge from 'highcharts/modules/solid-gauge';
import * as Highcharts from 'highcharts/highcharts';
import * as HighchartsMore from 'highcharts/highcharts-more';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../../chartService';

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
  public messageCount: number;
  public chart: any;
  private beScore: number;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService) {
    this.messageCount = 0;
  }

  ngOnInit(): void {
    this.chartService.getBEScore()
      .subscribe(data => {
        console.log(data);
        this.beScore = data.content;
        this.initChart(this.buildGauge());
      }, error => console.log(error));
  }

  ngOnDestroy() {
    // this.updataDataEndRef.unsubscribe();
  }

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
      credits: {
        enabled: false
      }
      ,
      tooltip: {
        enabled: false
      },
      yAxis: {
        /* min: 0,
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
        tickColor: '#666', */

        min: 0,
        max: 55,

        minorTickInterval: 'auto',
        minorTickWidth: 1,
        minorTickLength: 25,
        minorTickPosition: 'outside',
        minorTickColor: '#0052D4',

        tickPixelInterval: 30,
        tickWidth: 1,
        tickPosition: 'outside',
        tickLength: 40,
        tickColor: '#0052D4',
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
        plotBands:  [{
          from: 0,
          to: 100,
          color: 'rgba(58, 154, 252, 0.6)' // green
      }, {
          from: 100,
          to: 160,
          color: 'rgba(58, 154, 252, 0.4)' // yellow
      }, {
          from: 160,
          to: 200,
          color: 'rgba(58, 154, 252, 0.2)' // red
      }]
      },
      series: [{
        name: 'Speed',
        data: [this.beScore],
        tooltip: {
          valueSuffix: ' km/h'
        }
      }]
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
