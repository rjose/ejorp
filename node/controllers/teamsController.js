var Logger = require('../lib/logger')
  , Utils = require('./utils')
  , _ = require('underscore')
;

function createTeam(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    var teamName = req.body.name;
    var userId = userInfo.userId;
    // TODO: Send info to ejorp engine
    res.json({message: 'Created team'}, 201);
  });
}

function inviteOthers(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    var emails = req.body.emails;
    var userId = userInfo.userId;
    var teamId = req.params.teamId;
    console.log("=====> team: " + teamId);
    // TODO: Send info to ejorp engine
    res.json({message: 'Created invitation'}, 200);
  });
}

function acceptInvitation(userId, teamId, inviteToken, callback) {
  // TODO: Check that inviteToken is valid
  // TODO: Call into ejorpEngine to accept the invitation
  callback(null);
}

function updateInvitation(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    var userId = userInfo.userId;
    var teamId = req.params.teamId;
    var inviteToken = req.params.inviteToken;

    switch(req.body.action) {
      case 'accept':
      acceptInvitation(userId, teamId, inviteToken, function(err) {
        if (err) {
          return res.json({message: 'Unauthorized'}, 403);
        }
        return res.json({message: 'Invitation accepted'}, 200);
      });  
      break;

      default:
      return res.json({message: 'Invalid request'}, 400);
    }
    
    // TODO: Send info to ejorp engine
    res.json({message: 'Created invitation'}, 200);
  });
}

function requestInvitation(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    var ownerEmail = req.body.email;
    // TODO: Send invitation request to ejorp-engine
    res.json({message: 'Requested invitation'}, 200);
  });
}


function acceptInvitationRequest(ownerId, teamId, inviteRequestToken, callback) {
  // TODO: send invitation request update to ejorp-engine
  callback(null);
}

function updateInvitationRequest(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    switch(req.body.action) {
      case 'accept':
      var inviteRequestToken = req.params.inviteRequestToken;
      var teamId = req.body.teamId;
      acceptInvitationRequest(userInfo.userId, teamId, inviteRequestToken, function(err) {
        // TODO: Handle error
        res.json({message: 'Invitation request accepted'}, 200);
      });
      break;

      default:
      return res.json({message: 'Invalid request'}, 400);
    }
  });
}

function deleteTeam(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    // TODO: Send request to ejorp engine. Should check that the user is the owner.
    res.json({message: 'Deleted team'}, 200);
  });
}


function leaveTeam(teamId, userId, callback) {
  // TODO: Send info to ejorp engine
  return callback(null);
}


function evictMember(teamId, userId, evictedMemberId, callback) {
  // TODO: Send info to ejorp engine
  return callback(null);
}

function updateTeam(req, res, next) {
  Utils.performAuthenticatedRequest(req, res, next, function(userInfo) {
    switch(req.body.action) {
      case 'leave':
      leaveTeam(req.params.teamId, userInfo.userId, function(err) {
        // TODO: Handle error
        res.json({message: 'Left team'}, 200);
      });
      break;

      case 'evict':
      evictMember(req.params.teamId, userInfo.userId, req.body.memberId, function(err) {
        // TODO: Handle error
        res.json({message: 'Evicted member'}, 200);
      });
      break;

      default:
      return res.json({message: 'Invalid request'}, 400);
    }
    // TODO: Send request to ejorp engine. Should check that the user is the owner.
  });
}

function addRoutes(app, prefix) {
  app.post(prefix + 'teams', createTeam);
  app.post(prefix + 'teams/:teamId/invitation', inviteOthers);
  app.put(prefix + 'teams/:teamId/invitation/:inviteToken', updateInvitation);

  app.post(prefix + 'teams/invitation-request', requestInvitation);
  app.put(prefix + 'teams/invitation-request/:inviteRequestToken', updateInvitationRequest);

  app.delete(prefix + 'teams/:teamId', deleteTeam);
  app.put(prefix + 'teams/:teamId', updateTeam);
}

var interface = {
  addRoutes: addRoutes
};

module.exports = interface;
