import * as types from '../actions/actionTypes';
import {ListView} from 'react-native';

var ds = new ListView.DataSource({ rowHasChanged:(r1,r2) => r1.place_id !== r2.place_id });
var initialState = {isLoading:false, isLoadingDB: false, searchString: '', addresses:ds.cloneWithRows([]), favourites:ds.cloneWithRows([])};



export default function addressesByGeoEncoding(state = initialState, action = {}){
  // console.log("Receiver executed for action : " + action.type);
  switch (action.type) {
    case types.CHANGE_SEARCH_TEXT:
        return Object.assign({},state, { searchString: action.searchString});
    case types.REQUEST_ADDRESS:
        return Object.assign({},state, { isLoading:true});
    case types.RECEIVE_ADDRESS:
      return Object.assign({},state, { isLoading:false, addresses: ds.cloneWithRows(action.addresses)});
    case types.REQUEST_DB_DATA:
        return Object.assign({},state, { });
    case types.RECEIVE_DB_DATA:
      return Object.assign({},state, { favourites: ds.cloneWithRows(action.results)});
    case types.RESET_STATE:
      return Object.assign({},state, initialState);
    case types.ROW_PRESS:
      action.routerAction()
      return state;
    case types.ROUTE_POP:
      // console.log("POP : " +  action);
      action.fnPop();
      return state;
    default:
      return state;
  }
}
