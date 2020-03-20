// tslint:disable-next-line:ban-types
declare let gtag: Function;

export class GoogleAnalyticsService {

  constructor() {
  }

  public eventEmitter(
    eventName: string,
    eventCategory: string,
    eventAction: string,
    eventLabel: string = null,
    eventValue: number = null) {
    gtag('event', eventName, {
      eventCategory,
      eventLabel,
      eventAction,
      eventValue
    });
  }
}
