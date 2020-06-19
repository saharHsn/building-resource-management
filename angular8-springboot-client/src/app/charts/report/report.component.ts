import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ChartService} from '../chartService';
import * as html2pdf from 'html2pdf.js';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
// bullet values
  energyBullet: any;
  target: number;
  /* target:number; */
  currentEnergy: number;
  lastEnergy: number;
  lastMonth: string;
  beScore: number;
  nationalMedian: number;
  propertyTarget: number;

  constructor(private route: ActivatedRoute, private router: Router,
              private chartService: ChartService) {

    this.initchart();
  }

  ngOnInit() {
  }

  download() {
    this.chartService.download()
      .subscribe(response => {
        this.downLoadFile(response, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;');
      });
  }

  downLoadFile(data: any, type: string) {
    const blob = new Blob([data], {type});
    // window.open(this.restUrl, "_blank");
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url, '_blank');
    if (!pwa || pwa.closed || typeof pwa.closed === 'undefined') {
      alert('Please disable your Pop-up blocker and try again.');
    }
  }

  // module npm used for this html2pdf.js
  downloadPdf() {
    const options = {
      filename: 'report.pdf',
      image: {type: 'png'}, // png
      html2canvas: {},
      jsPDF: {format: 'a2', orientation: 'portrait'}

    };
    const content: Element = document.getElementById('content');
    html2pdf()
      .from(content)
      .set(options)
      .save();
  }

  print() {
    window.print();
  }

  loadPage(): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() =>
      this.router.navigate(['report']));
  }

  setBulletValues(arg): number {
    let value;
    switch (arg) {
      case 'APlus':
        value = 7.5;
        break;
      case 'A':
        value = 6.5;
        break;
      case 'B':
        value = 5.5;
        break;
      case 'BMinus':
        value = 4.5;
        break;
      case 'C':
        value = 3.5;
        break;
      case 'D':
        value = 2.5;
        break;
      case 'E':
        value = 1.5;
        break;
      case 'F':
        value = .5;
        break;
      default:
        value = 0;
        break;
    }
    return value;
  }

  initchart() {
    this.chartService.lastMonthSummary()
      .subscribe(data => {
        this.lastMonth = data.content.lastMonth;
      }, error => console.log(error));
    this.chartService.getBEScore()
      .subscribe(data => {
        this.beScore = data.content;
      }, error => console.log(error));
    this.chartService.getNationalMedian()
      .subscribe(data => {
        this.nationalMedian = data.content;
      }, error => console.log(error));
    this.chartService.getPropertyTarget()
      .subscribe(data => {
        this.propertyTarget = data.content;
      }, error => console.log(error));
    /*  */
  }
}
