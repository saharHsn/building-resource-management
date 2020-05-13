import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ChartService } from '../chartService';
import * as PDF from "jspdf";
import * as html2pdf from 'html2pdf.js'
@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.css']
})
export class ReportComponent implements OnInit {
 /*  @ViewChild('content', { static: true }) content: ElementRef; */
  beScore: number;
  nationalMedian: number;
  propertyTarget: number;
  constructor(private route: ActivatedRoute, private router: Router,
    private chartService: ChartService) {
  }

  ngOnInit() {
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
  }
  download() {
    this.chartService.download()
      .subscribe(response => {
        this.downLoadFile(response, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;');
      });
  }

  downLoadFile(data: any, type: string) {
    const blob = new Blob([data], { type });
    // window.open(this.restUrl, "_blank");
    const url = window.URL.createObjectURL(blob);
    const pwa = window.open(url, '_blank');
    if (!pwa || pwa.closed || typeof pwa.closed === 'undefined') {
      alert('Please disable your Pop-up blocker and try again.');
    }
  }

  //module npm used for this html2pdf.js 
  downloadPdf(){
    const options={
      filename:'report.pdf',
      image:{type:'png'},//png 
      html2canvas:{},
      jsPDF:{format: 'a2',orientation:'portrait'}

    };
    const content:Element=document.getElementById('content');

    html2pdf()
    .from(content)
    .set(options)
    .save();
  }

  print(){
    window.print();
  }
}
 