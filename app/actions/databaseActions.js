var types = require('./actionTypes');
var addressActions = require('./addressActions');


function requestFavDB() {
  return {
    type: types.REQUEST_DB_DATA
  };
}

function receiveFavDB(results) {
  return {
    type: types.RECEIVE_DB_DATA,
    results
  };
}

function insertFavDB(index, id) {
  return {
    type: types.DB_FAV_ADDED,
    index,
    id
  };
}

function unFav(index, address) {
  return {
    type: types.DB_UNFAV,
    index,
    address
  };
}

function removeFav(index) {
  return {
    type: types.DB_FAV_REMOVED,
    index
  };
}

function dbUpdated(results) {
  return {
    type: types.DB_UPDATED,
  }
}

module.exports.insertFavourites = function(db, address, index){
    return dispatch=>{
      return db.insertAddress(address).then((id) => {
              dispatch(insertFavDB(index, id));
            })
            .catch((error) => {
                // // console.log("Action - DB ERROR " + error);
            })
    };
}

/**
 Called by AddressList - needs to remove favourite and update 'addresses' array
 */
module.exports.unFavourite = function(db, address, index){
    return dispatch=>{
      return db.removeFavourite(address).then(() => {
              dispatch(unFav(index, address));
            })
            .then(() => {
              //re-fetch favourites?
            })
            .catch((error) => {
                // // console.log("Action - DB ERROR " + error);
            })
    };
}




/**
Called from Favourites - needs to remove from DB and 'favourites' array
*/
module.exports.removeFavourite = function(db, id, index, searchAddresses){

// // console.log("REMOVING FAVS");
    return dispatch=>{

        return db.removeAddress(id)
          .then((tx, result) => {
            dispatch(removeFav(index));
            if(searchAddresses) {
              addressActions.checkAllFav(searchAddresses, db, dispatch);
            }
          })
          .catch((error) => {
            // // console.log("Action - DB REMOVE ERROR " + error);
          })
    };
}




module.exports.fetchFavourites = function(db){

// // console.log("FETCHING FAVS");
    return dispatch=>{
      dispatch(requestFavDB())

      return db.getFavourites().then((favourites) => {
          dispatch(receiveFavDB(favourites));
      })
      .catch((error) => {
        // // console.log("Action - DB ERROR " + error);
      })
    };
}
