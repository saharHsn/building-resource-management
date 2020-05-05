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
  id: any = 1;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private chartService: ChartService) {
    this.messageCount = 0;
    // this.id=this.chartService.id;
  }

  ngOnInit(): void {
    this.chartService.getBEScore()
      .subscribe(data => {
        this.beScore = Math.trunc(data.content);
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
        background: {
          backgroundColor: 'rgba(0,0,0,0)',


        }
      },
      credits: {
        enabled: false
      }
      ,
      tooltip: {
        enabled: false
      },
      yAxis: {


        min: 0,
        max: 100,

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

        plotBands: [{ // mark the weekend
          color: {
            linearGradient: [0, 0, 300, 0],
            stops: [
              [0.4, '#DF5353'],
              [0.7, '#DDDF0D'],
              [0.95, '#55BF3B'], // green
              // yellow
              // red
            ]
          },
          from: 0,
          to: 100
        }]
      },
      plotOptions: {
        gauge: {
          dataLabels: {
            y: 10,
            borderWidth: 0,
            useHTML: true
          }
        }
      },

      series: [{
        name: 'Speed',
        data: [this.beScore],
        dataLabels: {
          format:
            '<div style="text-align:center">' +
            '<span style="font-size:30px">{y}</span><br/>' +
            '<span style="font-size:12px;opacity:0.4">' +

            '</span>' +
            '</div>'
        }
      }]
    };
  }

  private initChart(gaugeOptions: any) {
    this.chart = Highcharts.chart('gauge-container', gaugeOptions);
  }
}
