import {Component, OnInit} from '@angular/core';

import * as Highcharts from 'highcharts';

import HC_map from 'highcharts/modules/map';
import acce from 'highcharts/modules/accessibility';
import {ChartService} from '../../chartService';
import {TimeServicesService} from '../../../_services/time-services.service';

HC_map(Highcharts);
acce(Highcharts);

@Component({
    selector: 'app-heatmap-daily',
    templateUrl: './heatmap-daily.component.html',
    styleUrls: ['./heatmap-daily.component.css']
})
export class HeatmapDailyComponent implements OnInit {

    Highcharts = Highcharts;
    loading = true;
    chartOptions: any;
    matrixData = [[]];
    /*  year:string; */
    months: any = this._timeServices.getMonths();
    years: any = this._timeServices.getYears();
    monthChart = this._timeServices.currentMonth();
    yearChart = this._timeServices.currentYear();
    loadingBar = false;
    private getIndexOfWeekDay(dayOfWeek: any) {
        switch (dayOfWeek) {
            case 'MONDAY':
                return 0;
            case 'TUESDAY':
                return 1;
            case 'WEDNESDAY':
                return 2;
            case 'THURSDAY':
                return 3;
            case 'FRIDAY':
                return 4;
            case 'SATURDAY':
                return 5;
            case 'SUNDAY':
                return 6;
        }
    }

    private getIndexOfMonth(month: any) {
        switch (month) {
            case 'JANUARY':
                return 0;
            case 'FEBRUARY':
                return 1;
            case 'MARCH':
                return 2;
            case 'APRIL':
                return 3;
            case 'MAY':
                return 4;
            case 'JUNE':
                return 5;
            case 'JULY':
                return 6;
            case 'AUGUST':
                return 7;
            case 'SEPTEMBER':
                return 8;
            case 'OCTOBER':
                return 9;
            case 'NOVEMBER':
                return 10;
            case 'DECEMBER':
                return 11;
        }
    }




    constructor(private chartService: ChartService, private _timeServices: TimeServicesService) {

    }
    ngOnInit(): void {
        this.initChart(this.yearChart);
    }



    create() {
        this.loadingBar = true;
        this.initChart(this.yearChart);
      }

    initChart(year): void {
        this.chartService.getHeatMapDaily(year).subscribe(data => {


            for (const datum of data) {
                this.matrixData.push([this.getIndexOfMonth(datum.month), this.getIndexOfWeekDay(datum.dayOfWeek), datum.consumption]);
            }
            this.loadingBar = false;

            this.chartOptions = {
                chart: {
                    type: 'heatmap',
                    marginTop: 40,
                    marginBottom: 80,
                    plotBorderWidth: 1
                },



                title: {
                    text: 'Average Monthly Consumption Heat Map'
                },

                xAxis: {
                    categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'], title: {
                        text: 'Months'
                    }
                },

                yAxis: {
                    categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'], title: {
                        text: 'Days'
                    },

                    reversed: true,
                    tickInterval: 1,
                    startOnTick: false,
                    endOnTick: false,
                },

                accessibility: {
                    point: {
                        descriptionFormatter(point) {
                            let ix = point.index + 1,
                                xName = this.getPointCategoryName(point, 'x'),
                                yName = this.getPointCategoryName(point, 'y'),
                                val = point.value;
                            return ix + '. ' + xName + ' sales ' + yName + ', ' + val + '.';
                        }
                    }
                },

                colorAxis: {
                    linearGradient: [300, 300, 300, 0],
                    stops: [
                        [0.1, '#55BF3B'],
                        [0.6, '#FFF700'],
                        [0.7, '#FFF700'],
                        [0.95, '#FF0000'], // green
                        // yellow
                        // red
                    ]
                },

                legend: {
                    align: 'right',
                    layout: 'vertical',
                    margin: 0,
                    verticalAlign: 'top',
                    y: 25,
                    symbolHeight: 280
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: '  Value {point.value}',

                },
                credits: {
                    enabled: false
                },
                series: [{
                    name: 'Daily consumption',
                    borderWidth: 1,
                    data: this.matrixData,
                    dataLabels: {
                        enabled: false,
                        color: '#000000'
                    }
                }],

                /*   responsive: {
                      rules: [{
                          condition: {
                              maxWidth: 500
                          },
                          chartOptions: {
                              yAxis: {
                                  labels: {
                                      formatter: function () {
                                          return this.value.charAt(0);
                                      }
                                  }
                              }
                          }
                      }]
                  } */

            };
        });
    }
}
